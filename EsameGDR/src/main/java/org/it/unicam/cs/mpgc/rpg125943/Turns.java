package org.it.unicam.cs.mpgc.rpg125943;

import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

public class Turns {

    private final BattleEngine battleEngine;
    private final BattleEngine.BattleListener listener;
    private final Player player;
    private final Iterator<Enemy> remainingEnemies;
    private final Consumer<Enemy> onEnemyAppear;
    private final Consumer<Boss> onBossAppear;

    private Entity currentOpponent;
    private boolean fightingBoss;
    private boolean finished;
    private boolean won;

    public Turns(BattleEngine battleEngine, Player player, List<Enemy> normalEnemies, BattleEngine.BattleListener listener, Consumer<Enemy> onEnemyAppear, Consumer<Boss> onBossAppear) {
        this.battleEngine = battleEngine;
        this.player = player;
        this.remainingEnemies = normalEnemies.iterator();
        this.listener = listener;
        this.onEnemyAppear = onEnemyAppear;
        this.onBossAppear = onBossAppear;
        pickNextOpponent();
    }

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
            player.heal();
            pickNextOpponent();
        }

    }
    private void pickNextOpponent() {
        if (remainingEnemies.hasNext()) {
            Enemy enemy = remainingEnemies.next();
            currentOpponent = enemy;
            if (onEnemyAppear != null) {
                onEnemyAppear.accept(enemy);
            }
        } else {
            fightingBoss = true;
            Boss boss = chooseBoss();
            currentOpponent = boss;
            if (onBossAppear != null) {
                onBossAppear.accept(boss);
            }
        }
    }

    private Boss chooseBoss() {
        Boss[] bosses = { Boss.bigBoss(), Boss.Joe(), Boss.vas(), Boss.dutch() };
        return bosses[(int) (Math.random() * bosses.length)];
    }

    public Entity getCurrentOpponent() {
        return currentOpponent;
    }

    public boolean isFinished() {
        return finished;
    }

    public boolean hasWon() {
        return won;
    }

}

