package org.it.unicam.cs.mpgc.rpg125943;

import java.util.Random;

abstract public class Entity {

    private String name;
    protected int stamina;
    protected int attack;
    protected int defense;
    protected int speed;
    protected int level;
    protected double exp;
    private boolean alive=true;


    private static final double dodgePerSpeed=0.015;
    private static final double dodgeRelativo=0.015;
    private static final double dodgeCap=0.75;
    private static final double dodgeMin=0.05;

    private static final Random RANDOM = new Random();



    protected Entity(String name, int stamina, int attack, int defense, int speed , int level, double exp, boolean alive) {
        this.name = name;
        this.stamina = stamina;
        this.attack = attack;
        this.defense = defense;
        this.speed = speed;
        this.level = level;
        this.exp = exp;
        this.alive = alive;
    }

    public double dodgeChance(Entity attacker) {
        double baseDodge = this.speed * dodgePerSpeed;
        double modificatore = (this.speed - attacker.getSpeed()) * dodgeRelativo;
        double totalDodge = baseDodge + modificatore;

        return Math.max(dodgeMin, Math.min(dodgeCap, totalDodge));
    }

    public void attack (Entity target) {
        double evasione = target.dodgeChance(this);
        if (RANDOM.nextDouble() < evasione){
            System.out.println(target.getName() + " ha schivato l'attacco di " + this.getName() + "!");
            return;
        }

        double reductionFactor = 100 / (100.0 + target.getDefense());
        int damage = Math.max(1, (int) Math.round(this.attack* reductionFactor));

        target.takeDamage(damage);
        System.out.println(this.getName() + " ha attaccato " + target.getName() + " causando " + damage + " danni!");

        if (!target.isAlive() && this instanceof Esperienza attacker) {
            attacker.gainExp(target.getExp());
        }

    }

    public void takeDamage(int damage) {
        this.stamina -= damage;
        if (this.stamina <= 0) {
            this.stamina = 0;
            this.alive = false;
        }
    }

    public boolean isAlive() {
        return alive;
    }

    public String getName() {
        return name;
    }

    public int getStamina() {
        return stamina;
    }

    public int getAttack() {
        return attack;
    }

    public int getDefense() {
        return defense;
    }

    public int getSpeed() {
        return speed;
    }

    public int getLevel() {
        return level;
    }

    public double getExp() {
        return exp;
    }

}
