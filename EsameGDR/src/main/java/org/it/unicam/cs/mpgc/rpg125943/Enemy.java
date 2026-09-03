package org.it.unicam.cs.mpgc.rpg125943;

import java.util.Random;

public class Enemy extends Entity {

    private static final Random RANDOM = new Random();

    protected Enemy(String name, int stamina, int attack, int defense, int speed, int level, double exp, boolean alive) {
        super(name, stamina, attack, defense, speed, level, exp, alive);
    }


    public static Enemy nemicoCasuale(String name, int level){
        int stamina = 15 + RANDOM.nextInt(36) + (level * 10);
        int attack = 5 + RANDOM.nextInt(26) + (level * 2);
        int defense = 5 + RANDOM.nextInt(21) + level;
        int speed = 5 + RANDOM.nextInt(11);
        int exp = 10 + RANDOM.nextInt(11) + (level * 2);
        return new Enemy(name, stamina, attack, defense, speed, level, exp, true);
    }


}
