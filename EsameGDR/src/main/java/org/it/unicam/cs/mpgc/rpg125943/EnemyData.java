package org.it.unicam.cs.mpgc.rpg125943;

/**
 * Stesso principio di {@code PlayerData}, ma per un nemico o un boss: la
 * forma salvabile in JSON, con le statistiche esatte al momento del
 * salvataggio.
 */
public class EnemyData {

    private String name;
    private int stamina;
    private int maxStamina;
    private int attack;
    private int defense;
    private int speed;
    private int level;
    private double exp;
    private boolean bossType;


    public EnemyData() {
    }



    public EnemyData(String name, int stamina, int maxStamina, int attack, int defense, int speed, int level, double exp, boolean bossType) {
        this.name = name;
        this.stamina = stamina;
        this.maxStamina = maxStamina;
        this.attack = attack;
        this.defense = defense;
        this.speed = speed;
        this.level = level;
        this.exp = exp;
        this.bossType = bossType;

    }

    /**
     * Cattura lo stato esatto di un nemico o di un boss cosi' com'e' in quel
     * momento, ricordando anche SE era un Boss.
     *
     * @param entity l'entita' da salvare (un Enemy o un Boss)
     * @return la forma salvabile corrispondente
     */
    public static EnemyData fromEntity(Entity entity) {
        return new EnemyData(
                entity.getName(),
                entity.getStamina(),
                entity.getMaxStamina(),
                entity.getAttack(),
                entity.getDefense(),
                entity.getSpeed(),
                entity.getLevel(),
                entity.getExp(),
                entity instanceof Boss
        );
    }

    /**
     * Ricostruisce l'entita' salvata: un Boss torna un Boss, un Enemy
     * normale torna un Enemy normale.
     *
     * @return una nuova entita' con le statistiche esatte salvate
     */
    public Enemy toEnemy() {
        if (bossType) {
            return new Boss(name, stamina, maxStamina, attack, defense, speed, level, exp, stamina > 0);
        }
        return new Enemy(name, stamina, maxStamina, attack, defense, speed, level, exp, stamina > 0);
    }

    public String getName() {
        return name;
    }

}
