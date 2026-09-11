package org.it.unicam.cs.mpgc.rpg125943;

public class PlayerData {

    private String name;
    private int stamina;
    private int maxStamina;
    private int attack;
    private int defense;
    private int speed;
    private int level;
    private double exp;
    private Styles style;

    public PlayerData() {
    }

    public PlayerData(String name, int stamina, int maxStamina, int attack, int defense, int speed, int level, double exp, Styles style) {
        this.name = name;
        this.stamina = stamina;
        this.maxStamina = maxStamina;
        this.attack = attack;
        this.defense = defense;
        this.speed = speed;
        this.level = level;
        this.exp = exp;
        this.style = style;
    }

    public static PlayerData fromPlayer(Player player) {
        return new PlayerData(player.getName(), player.getStamina(), player.getMaxStamina(), player.getAttack(), player.getDefense(), player.getSpeed(), player.getLevel(), player.getExp(), player.getStyle());
    }

    public Player toPlayer() {
        return new Player(name, stamina, maxStamina, attack, defense, speed, level, exp,stamina > 0, style);
    }

    public String getName() {
        return name;
    }

}
