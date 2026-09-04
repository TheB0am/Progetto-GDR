package org.it.unicam.cs.mpgc.rpg125943;

public class BattleEngine {

    public interface BattleListener {
        void onAttack(AttackResult result);

        void onBattleEnd(Entity winner, Entity loser);
    }

    //Questo metodo è il gestore dell battaglie, per ora la battaglia si svolge in un ordine predisposto,
    // in futuro si potrebbe implemntare un sistema di volonta/iniziativa per decidere l'ordine di attacco di turno in turno.
    public void runBattle(Player player, Entity enemy, BattleListener listener) {
        while (player.isAlive() && enemy.isAlive()) {
            AttackResult result = player.attack(enemy);
            listener.onAttack(result);

            if (enemy.isAlive()) {
                result = enemy.attack(player);
                listener.onAttack(result);
            }
        }

            Entity winner = player.isAlive() ? player : enemy;
            Entity loser = player.isAlive() ? enemy : player;
            listener.onBattleEnd(winner, loser);

    }

}
