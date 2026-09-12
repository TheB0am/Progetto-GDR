package org.it.unicam.cs.mpgc.rpg125943;


/**
 * I 4 stili di combattimento. Ogni stile porta con se' le proprie statistiche
 * di partenza e i propri incrementi ad ogni level up.
 * <p>
 * PRIMA: {@code Player.levelUp()} e {@code App.createPlayer()} avevano
 * ciascuno uno switch sui 4 stili.
 * <p>
 * Tutte le informazioni specifiche di uno stile vivono qui. Aggiungere
 * un nuovo stile significa aggiungere una nuova costante in questo enum;
 * {@code Player} e la UI non vanno piu' toccati.
 */
public enum Styles {

    BRAWLER    (150, 8, 10, 5,  25, 3, 2, 2),
    IN_FIGHTER (100, 20, 5, 8,  10, 8, 1, 2),
    DEFENSE_LAB(100, 5, 40, 12,  5, 1, 10, 3),
    OUT_BOXER  (50, 10, 8, 20,   5, 1, 1, 4);

    //Statistiche di partenza
    private final int baseStamina;
    private final int baseAttack;
    private final int baseDefense;
    private final int baseSpeed;

    //Incrementi ad ogni level up
    private final int staminaGrowth;
    private final int attackGrowth;
    private final int defenseGrowth;
    private final int speedGrowth;

    Styles(int baseStamina, int baseAttack, int baseDefense, int baseSpeed,
           int staminaGrowth, int attackGrowth, int defenseGrowth, int speedGrowth) {
        this.baseStamina = baseStamina;
        this.baseAttack = baseAttack;
        this.baseDefense = baseDefense;
        this.baseSpeed = baseSpeed;
        this.staminaGrowth = staminaGrowth;
        this.attackGrowth = attackGrowth;
        this.defenseGrowth = defenseGrowth;
        this.speedGrowth = speedGrowth;
    }

    public int getBaseStamina() {
        return baseStamina;
    }

    public int getBaseAttack() {
        return baseAttack;
    }

    public int getBaseDefense() {
        return baseDefense;
    }

    public int getBaseSpeed() {
        return baseSpeed;
    }

    public int getStaminaGrowth() {
        return staminaGrowth;
    }

    public int getAttackGrowth() {
        return attackGrowth;
    }

    public int getDefenseGrowth() {
        return defenseGrowth;
    }

    public int getSpeedGrowth() {
        return speedGrowth;
    }
}
