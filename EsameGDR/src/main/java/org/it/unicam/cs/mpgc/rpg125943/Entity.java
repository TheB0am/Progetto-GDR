package org.it.unicam.cs.mpgc.rpg125943;

abstract public class Entity {

    private String name;
    protected int stamina;
    protected int attack;
    protected int defense;
    protected int level;
    protected double exp;
    private boolean alive=true;



    protected Entity(String name, int stamina, int attack, int defense, int level, double exp, boolean alive) {
        this.name = name;
        this.stamina = stamina;
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

    public int getLevel() {
        return level;
    }

    public double getExp() {
        return exp;
    }

}
