package org.it.unicam.cs.mpgc.rpg125943.engine.auto;

import org.it.unicam.cs.mpgc.rpg125943.engine.turns.Turns;
import org.it.unicam.cs.mpgc.rpg125943.engine.BattleEngine;
import org.it.unicam.cs.mpgc.rpg125943.model.Boss;
import org.it.unicam.cs.mpgc.rpg125943.model.Enemy;
import org.it.unicam.cs.mpgc.rpg125943.model.Opponents;
import org.it.unicam.cs.mpgc.rpg125943.model.Player;

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
            Boss boss = Opponents.randomBoss();

            if (onBossAppear != null) {
                onBossAppear.accept(boss);
            }

            battleEngine.runBattle(player, boss, listener);
        }

        return player.isAlive();
    }

}
