package org.it.unicam.cs.mpgc.rpg125943;

public class Boss extends Enemy {


    protected Boss(String name, int stamina, int attack, int defense, int speed, int level, double exp, boolean alive) {
        super(name, stamina, attack, defense, speed, level, exp, alive);
    }

    public static Boss bigBoss(String name, int level){
        int stamina = 300;
        int attack = 25 ;
        int defense = 25 ;
        int speed = 15 ;
        int exp = 250 ;
        return new Boss("BigBoss", stamina, attack, defense, speed, level, exp, true);
    }

    public static Boss Joe(String name, int level){
        int stamina = 150;
        int attack = 40 ;
        int defense = 15 ;
        int speed = 25 ;
        int exp = 250 ;
        return new Boss("Joe Yabuki", stamina, attack, defense, speed, level, exp, true);
    }

    public static Boss vas(String name, int level){
        int stamina = 150;
        int attack = 15 ;
        int defense = 40 ;
        int speed = 25 ;
        int exp = 250 ;
        return new Boss("Vas", stamina, attack, defense, speed, level, exp, true);
    }

    public static Boss dutch(String name, int level){
        int stamina = 100;
        int attack = 25 ;
        int defense = 25 ;
        int speed = 40 ;
        int exp = 250 ;
        return new Boss("Dutch Van Der Linde", stamina, attack, defense, speed, level, exp, true);
    }



}
