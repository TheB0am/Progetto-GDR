package org.it.unicam.cs.mpgc.rpg125943;

import java.util.List;
import java.util.function.Consumer;

public class GameSession {

    private final BattleEngine battleEngine;

    public GameSession(BattleEngine battleEngine) {this.battleEngine = battleEngine;}


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
            Boss boss = chooseBoss();

            if (onBossAppear != null) {
                onBossAppear.accept(boss);
            }

            battleEngine.runBattle(player, boss, listener);
        }

        return player.isAlive();
    }

    private Boss chooseBoss() {
        Boss[] bosses = { Boss.bigBoss(), Boss.Joe(), Boss.vas(), Boss.dutch() };
        return bosses[(int) (Math.random() * bosses.length)];
    }


    public static List<Enemy> defaultEnemies() {
        return List.of(
                Enemy.nemicoCasuale("Vincenzo", 1),
                Enemy.nemicoCasuale("Glad0s", 1),
                Enemy.nemicoCasuale("Soap", 1),
                Enemy.nemicoCasuale("Boros", 2),
                Enemy.nemicoCasuale("Looter", 2),
                Enemy.nemicoCasuale("Pasta", 2),
                Enemy.nemicoCasuale("Dogmeat", 3),
                Enemy.nemicoCasuale("Vladimir", 3),
                Enemy.nemicoCasuale("Mob", 3),
                Enemy.nemicoCasuale("Freeman", 4),
                Enemy.nemicoCasuale("B", 4),
                Enemy.nemicoCasuale("C", 4),
                Enemy.nemicoCasuale("John Box", 5)
        );
    }
}
