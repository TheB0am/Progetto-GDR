package org.it.unicam.cs.mpgc.rpg125943;

import java.util.List;
import java.util.function.Consumer;

/**
 * Versione A TURNI della sessione di gioco: a differenza di
 * {@link GameSession} (che combatte tutto in automatico), qui ogni chiamata
 * ad {@link #attack()} esegue UN solo scambio (player attacca, poi il
 * nemico risponde). E' pensata per essere guidata da un bottone "Attacca"
 * nella UI.
 * <p>
 * {@code enemyIndex} tiene traccia di quanti nemici normali sono gia' stati
 * battuti (ed e' anche l'indice del nemico che si sta affrontando ora, se
 * ancora vivo). Quando {@code enemyIndex} arriva alla fine della lista, si
 * passa al boss.
 */
public class Turns {

    private final BattleEngine battleEngine;
    private final BattleEngine.BattleListener listener;
    private final Player player;
    private final List<Enemy> enemies;
    private final Consumer<Enemy> onEnemyAppear;
    private final Consumer<Boss> onBossAppear;

    private int enemyIndex;
    private Entity currentOpponent;
    private boolean fightingBoss;
    private boolean finished;
    private boolean won;

    //Costruttore "normale": inizia dal primo nemico della lista.
    public Turns(BattleEngine battleEngine,
                 Player player,
                 List<Enemy> enemies,
                 BattleEngine.BattleListener listener,
                 Consumer<Enemy> onEnemyAppear,
                 Consumer<Boss> onBossAppear) {
        this(battleEngine, player, enemies, 0, listener, onEnemyAppear, onBossAppear);
    }

    /**
     * Come il costruttore base, ma si puo' scegliere da che nemico ripartire.
     *
     * @param startIndex indice del nemico da cui partire (0 = dal primo)
     */
    public Turns(BattleEngine battleEngine,
                 Player player,
                 List<Enemy> enemies,
                 int startIndex,
                 BattleEngine.BattleListener listener,
                 Consumer<Enemy> onEnemyAppear,
                 Consumer<Boss> onBossAppear) {
        this.battleEngine = battleEngine;
        this.player = player;
        this.enemies = enemies;
        this.enemyIndex = startIndex;
        this.listener = listener;
        this.onEnemyAppear = onEnemyAppear;
        this.onBossAppear = onBossAppear;
        pickNextOpponent();
    }

    /**
     * Costruttore per RIPRENDERE una partita salvata: a differenza degli
     * altri due, non sceglie un nuovo avversario (che per il boss sarebbe
     * scelto a caso) ma riprende esattamente {@code resumedOpponent}, con
     * le statistiche (ed eventuale danno subito) cosi' come erano al
     * salvataggio.
     *
     * @param startIndex      indice del nemico da cui ripartire (o {@code enemies.size()} se si era gia' al boss)
     * @param resumedOpponent l'avversario esatto da cui ripartire, con le sue statistiche esatte
     */
    public Turns(BattleEngine battleEngine,
                 Player player,
                 List<Enemy> enemies,
                 int startIndex,
                 Entity resumedOpponent,
                 BattleEngine.BattleListener listener,
                 Consumer<Enemy> onEnemyAppear,
                 Consumer<Boss> onBossAppear) {
        this.battleEngine = battleEngine;
        this.player = player;
        this.enemies = enemies;
        this.enemyIndex = startIndex;
        this.listener = listener;
        this.onEnemyAppear = onEnemyAppear;
        this.onBossAppear = onBossAppear;
        this.fightingBoss = startIndex >= enemies.size();
        this.currentOpponent = resumedOpponent;
    }

    /**
     * Esegue UN turno: chiamato dal bottone "Attacca" nella UI. Il player
     * attacca l'avversario corrente, che risponde se e' ancora vivo. Se lo
     * scontro finisce, avanza al prossimo nemico (o dichiara la partita
     * vinta/persa, a seconda di chi e' stato sconfitto).
     */
    public void attack(){
        if (finished || currentOpponent == null) { return;}

        boolean battleContinues = battleEngine.playerTurn(player, currentOpponent, listener);
        if (battleContinues) {
            return;
        }

        if (!player.isAlive()) {
            finished = true;
            won = false;
            return;
        }

        if (fightingBoss) {
            finished = true;
            won = true;
        } else {
            enemyIndex++;
            player.heal();
            pickNextOpponent();
        }

    }

    /**
     * Sceglie il prossimo avversario in base a {@code enemyIndex}: se ci
     * sono ancora nemici nella lista prende il prossimo, altrimenti passa
     * al boss. Usa {@link GameSession#randomBoss()} invece di avere una
     * propria copia duplicata della stessa logica di scelta del boss.
     */
    private void pickNextOpponent() {
        if (enemyIndex < enemies.size()) {
            Enemy enemy = enemies.get(enemyIndex);
            currentOpponent = enemy;
            if (onEnemyAppear != null) {
                onEnemyAppear.accept(enemy);
            }
        } else {
            fightingBoss = true;
            Boss boss = GameSession.randomBoss();
            currentOpponent = boss;
            if (onBossAppear != null) {
                onBossAppear.accept(boss);
            }
        }
    }



    public Entity getCurrentOpponent() {
        return currentOpponent;
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }

    public int getEnemyIndex() {
        return enemyIndex;
    }

    public boolean isFightingBoss(){
        return fightingBoss;
    }

    public boolean isFinished() {
        return finished;
    }

    public boolean hasWon() {
        return won;
    }

}

