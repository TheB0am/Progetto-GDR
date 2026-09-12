package org.it.unicam.cs.mpgc.rpg125943;

import java.util.List;
import java.util.function.Consumer;


/**
 * Gestisce una partita in modalita' AUTOMATICA: combatte tutti i nemici
 * della lista uno dopo l'altro (curando il player tra uno e l'altro),
 * poi il boss finale, tutto senza bisogno di premere niente ad ogni turno.
 * Usata solo da {@link Main} (la versione console) - la UI JavaFX usa
 * {@link Turns} invece, che va a turni singoli.
 */
public class GameSession {

    private final BattleEngine battleEngine;

    public GameSession(BattleEngine battleEngine) {this.battleEngine = battleEngine;}


    /**
     * Gioca l'intera sessione (nemici + boss finale) in un colpo solo.
     *
     * @param player        il player che combatte
     * @param normalEnemies la sequenza di nemici normali da affrontare in ordine
     * @param listener      riceve gli eventi di combattimento (onAttack, onBattleEnd)
     * @param onEnemyAppear callback opzionale, chiamata prima di ogni combattimento normale
     * @param onBossAppear  callback opzionale, chiamata quando compare il boss finale
     * @return true se il player ha vinto l'intera sessione (boss compreso), false altrimenti
     */
    public boolean play(Player player,
                        List<Enemy> normalEnemies,
                        BattleEngine.BattleListener listener,
                        Consumer<Enemy> onEnemyAppear,
                        Consumer<Boss> onBossAppear) {

        for (Enemy enemy : normalEnemies) {
            if(!player.isAlive()) break;

            if (onEnemyAppear != null) {
                onEnemyAppear.accept(enemy);
            }

            battleEngine.runBattle(player, enemy, listener);

            if (player.isAlive()) {
                player.heal();
            }
        }

        if(player.isAlive()) {
            Boss boss = randomBoss();

            if (onBossAppear != null) {
                onBossAppear.accept(boss);
            }

            battleEngine.runBattle(player, boss, listener);
        }

        return player.isAlive();
    }

    /**
     * Sceglie un boss a caso tra i 4 disponibili. Pubblico e statico cosi'
     * anche {@link Turns} puo' usarlo, invece di avere una copia duplicata
     * della stessa logica in due classi diverse (prima violava il principio
     * DRY: aggiungere un quinto boss avrebbe richiesto modificare due file).
     *
     * @return un boss scelto casualmente tra quelli disponibili
     */
    public static Boss randomBoss() {
        Boss[] bosses = { Boss.bigBoss(), Boss.Joe(), Boss.vas(), Boss.dutch() };
        return bosses[(int) (Math.random() * bosses.length)];
    }


    /**
     * La lista standard di nemici, usata sia da {@link Main} che da
     * {@code App} (tramite {@link Turns}). Attenzione: ogni chiamata genera
     * nemici NUOVI con statistiche casuali diverse (vedi
     * {@link Enemy#nemicoCasuale(String, int)}), non sono sempre gli stessi.
     *
     * @return una nuova lista di 13 nemici con livelli crescenti
     */
    public static List<Enemy> defaultEnemies() {
        return List.of(
                Enemy.nemicoCasuale("Ciotta", 1),
                Enemy.nemicoCasuale("Glad0s", 1),
                Enemy.nemicoCasuale("JoJo", 1),
                Enemy.nemicoCasuale("Krilin", 1),
                Enemy.nemicoCasuale("Volg", 2),
                Enemy.nemicoCasuale("Geralt", 2),
                Enemy.nemicoCasuale("Connor", 2),
                Enemy.nemicoCasuale("Dogmeat", 3),
                Enemy.nemicoCasuale("Rayman", 3),
                Enemy.nemicoCasuale("Mob", 3),
                Enemy.nemicoCasuale("Doakes", 4),
                Enemy.nemicoCasuale("Mike Ehrmantraut", 4),
                Enemy.nemicoCasuale("Kimball Cho", 4),
                Enemy.nemicoCasuale("Dexter", 5)
        );
    }
}
