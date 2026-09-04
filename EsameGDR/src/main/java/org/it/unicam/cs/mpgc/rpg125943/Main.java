package org.it.unicam.cs.mpgc.rpg125943;

import java.util.ArrayList;
import java.util.List;
import java.util.PrimitiveIterator;
import java.util.Scanner;


public class Main implements BattleEngine.BattleListener {

    private static final Scanner SCANNER = new Scanner(System.in);
    private final BattleEngine battleEngine = new BattleEngine();

    public static void main(String[] args) {
        new Main().start();
    }

    private static Player choiceReader(String name, int choice) {
        return switch (choice) {
            case 1 -> Player.brawler(name);
            case 2 -> Player.inFighter(name);
            case 3 -> Player.defenseLab(name);
            case 4 -> Player.outBoxer(name);
            default -> throw new IllegalArgumentException("Scelta non valida");
        };
    }

    private static Player createPlayer() {
        System.out.println("Come ti chiami?");
        String name = SCANNER.nextLine();

        System.out.println("Scegli il tuo stile di combattimento:");
        System.out.println("1. Brawler");
        System.out.println("2. In-Fighter");
        System.out.println("3. Defense Lab");
        System.out.println("4. Out-Boxer");
        int choice = SCANNER.nextInt();
        SCANNER.nextLine();
        return choiceReader(name, choice);
    }

    private static Boss chooseBoss() {
        Boss[] bosses = { Boss.bigBoss(), Boss.Joe(), Boss.vas(), Boss.dutch() };
        return bosses[(int) (Math.random() * bosses.length)];
    }

    private void start() {
        System.out.println("Bevenuto nella WBC!!!");
        Player player = createPlayer();

        List<Enemy> normalEnemies = List.of(
                Enemy.nemicoCasuale("Vincenzo", 1),
                Enemy.nemicoCasuale("Boros", 2),
                Enemy.nemicoCasuale("Dogmeat",3),
                Enemy.nemicoCasuale("Freeman",4),
                Enemy.nemicoCasuale("John Box",5)
        );

        for (Enemy enemy : normalEnemies) {
            if (!player.isAlive()) break;
            System.out.println("Un nuovo nemico e' apparso: " + enemy.getName() + " (Livello: " + enemy.getLevel() + ")");
            battleEngine.runBattle(player, enemy, this);
        }

        if (player.isAlive()) {
            System.out.println("Ora dovrai sfidare il campione");
            Boss boss = chooseBoss();
            System.out.println("Il campione è: " + boss.getName());
            battleEngine.runBattle(player, boss, this);
        }

        if (player.isAlive()) {
            System.out.println("Congratulazioni sei il nuovo Campione del mondo!");
        } else {
            System.out.println("Game over");
        }

    }

    @Override
    public void onAttack(AttackResult result) {
        if (result.isDodged()) {
            System.out.println(result.getTarget().getName() + " Ha schivato l'attacco di " + result.getAttacker().getName());
        } else {
            System.out.println(result.getAttacker().getName() + " ha attaccato " + result.getTarget().getName() + " e ha inflitto " + result.getDamage() + " danni.");
        }
    }

    @Override
    public void onBattleEnd(Entity winner, Entity loser) {
        System.out.println(loser.getName() + " e' stato sconfitto da " + winner.getName() + "!");
    }
}