package org.it.unicam.cs.mpgc.rpg125943;

public class Boss extends Enemy {


    protected Boss(String name, int stamina, int maxStamina , int attack, int defense, int speed, int level, double exp, boolean alive) {
        super(name, stamina, maxStamina , attack, defense, speed, level, exp, alive);
    }

    public static Boss bigBoss(){
        int stamina = 300;
        int maxStamina = 300;
        int attack = 25 ;
        int defense = 25 ;
        int speed = 15 ;
        double exp = 250 ;
        return new Boss("BigBoss", stamina, maxStamina , attack, defense, speed, 10000, exp, true);
    }

    public static Boss Joe(){
        int stamina = 150;
        int maxStamina = 150;
        int attack = 40 ;
        int defense = 15 ;
        int speed = 25 ;
        double exp = 250 ;
        return new Boss("Joe Yabuki", stamina, maxStamina , attack, defense, speed, 10000, exp, true);
    }

    public static Boss vas(){
        int stamina = 150;
        int maxStamina = 150;
        int attack = 15 ;
        int defense = 40 ;
        int speed = 25 ;
        double exp = 250 ;
        return new Boss("Vas", stamina, maxStamina , attack, defense, speed, 10000, exp, true);
    }

    public static Boss dutch(){
        int stamina = 100;
        int maxStamina = 100;
        int attack = 25 ;
        int defense = 25 ;
        int speed = 40 ;
        double exp = 250 ;
        return new Boss("Dutch Van Der Linde", stamina, maxStamina , attack, defense, speed, 10000, exp, true);
    }



}
