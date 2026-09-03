package org.it.unicam.cs.mpgc.rpg125943;

public class Player extends Entity {

    private int incrementalExp = 100;

    protected Player(String name, int stamina, int attack, int defense, int level, double exp, boolean alive) {
        super(name, stamina, attack, defense, level, exp, alive);
    }

    public static Player inFighter(String name) {
        return new Player(name, 50, 20, 10, 1, 0.0, true);
    }

    public static Player longGuard(String name) {
        return new Player(name, 100, 5, 20, 1, 0.0, true);
    }

    public static Player outBoxer(String name) {
        return new Player(name, 150, 10, 5, 1, 0.0, true);
    }

    private void gainExp(double exp) {
        this.exp += exp;
        if (this.exp >= incrementalExp) {
            this.levelUp();
            this.exp = 0;
            incrementalExp = (int) (incrementalExp * 1.1);
        }
    }

    private void levelUp() {
        this.level++;
        switch (this) {
            case Player player when this.getStamina() == 50 -> {
                this.attack += 5;
                this.defense += 2;
            }
            case Player player when this.getStamina() == 100 -> {
                this.stamina += 10;
                this.defense += 5;
            }
            case Player player when this.getStamina() == 150 -> {
                this.attack += 2;
                this.stamina += 25;
            }
            default -> {
            }
        }
    }
}
