package org.it.unicam.cs.mpgc.rpg125943;

public class EnemyData {

    private String name;
    private int stamina;
    private int maxStamina;
    private int attack;
    private int defense;
    private int speed;
    private int level;
    private double exp;

    public EnemyData() {
    }

    public EnemyData(String name, int stamina, int maxStamina, int attack, int defense, int speed, int level, double exp) {
        this.name = name;
        this.stamina = stamina;
        this.maxStamina = maxStamina;
        this.attack = attack;
        this.defense = defense;
        this.speed = speed;
        this.level = level;
        this.exp = exp;
    }

    public static EnemyData fromEntity(Entity entity) {
        return new EnemyData(
                entity.getName(),
                entity.getStamina(),
                entity.getMaxStamina(),
                entity.getAttack(),
                entity.getDefense(),
                entity.getSpeed(),
                entity.getLevel(),
                entity.getExp()
        );
    }

    public Enemy toEnemy() {
        return  new Enemy(name, stamina, maxStamina, attack, defense, speed, level, exp, stamina > 0);
    }

    public String getName() {
        return name;
    }

}
