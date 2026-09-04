package org.it.unicam.cs.mpgc.rpg125943;

public class AttackResult {

    private final Entity attacker;
    private final Entity target;
    private final boolean dodged;
    private final int damage;
    private final boolean targetDefeated;

    public AttackResult(Entity attacker, Entity target, boolean dodged, int damage, boolean targetDefeated) {
        this.attacker = attacker;
        this.target = target;
        this.dodged = dodged;
        this.damage = damage;
        this.targetDefeated = targetDefeated;
    }

    public Entity getAttacker() {
        return attacker;
    }

    public Entity getTarget() {
        return target;
    }

    public boolean isDodged() {
        return dodged;
    }

    public int getDamage() {
        return damage;
    }

    public boolean isTargetDefeated() {
        return targetDefeated;
    }

}

