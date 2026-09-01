package org.it.unicam.cs.mpgc.rpg125943;

abstract public class Entity {

    private String name;
    private int health;
    private int attack;
    private int defense;
    private int level;
    private double exp;
    private boolean alive=true;


    public Entity(String name, int health, int attack, int defense, int level, double exp, boolean alive) {
        this.name = name;
        this.health = health;
        this.attack = attack;
        this.defense = defense;
        this.level = level;
        this.exp = exp;
        this.alive = alive;
    }

    public void attack (Entity target) {
        int damage = this.attack - target.getDefense();
        if (damage > 0) {
            target.takeDamage(damage);
        }
    }

    public void takeDamage(int damage) {
        this.health -= damage;
        if (this.health <= 0) {
            this.health = 0;
            this.alive = false;
        }
    }

    public boolean isAlive() {
        return alive;
    }






    public String getName() {
        return name;
    }

    public int getHealth() {
        return health;
    }

    public int getAttack() {
        return attack;
    }

    public int getDefense() {
        return defense;
    }

    public int getLevel() {
        return level;
    }

    public double getExp() {
        return exp;
    }

}
