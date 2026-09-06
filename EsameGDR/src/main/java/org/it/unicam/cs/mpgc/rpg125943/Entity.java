package org.it.unicam.cs.mpgc.rpg125943;

import java.util.Random;

abstract public class Entity {

    private String name;
    protected int stamina;
    protected int maxStamina;
    protected int attack;
    protected int defense;
    protected int speed;
    protected int level;
    protected double exp;  //Exp verrà usata in maniera differente, player guadagna exp per aumentare di livello, il nemico invece garantirà quell'exp una volta sconfitto.
    private boolean alive=true;


    //Parametri utilizzati per il bilanciamento del sistema di schivata.
    private static final double dodgePerSpeed=0.015;
    private static final double dodgeRelativo=0.015;
    private static final double dodgeCap=0.75;
    private static final double dodgeMin=0.05;

    private static final Random RANDOM = new Random();



    protected Entity(String name, int stamina, int maxStamina, int attack, int defense, int speed , int level, double exp, boolean alive) {
        this.name = name;
        this.stamina = stamina;
        this.maxStamina = maxStamina;
        this.attack = attack;
        this.defense = defense;
        this.speed = speed;
        this.level = level;
        this.exp = exp;
        this.alive = alive;
    }

    public double dodgeChance(Entity attacker) {
        double baseDodge = this.speed * dodgePerSpeed; // probabilità di schivata basato sulla velocità dell'attacker
        double modificatore = (this.speed - attacker.getSpeed()) * dodgeRelativo; // garantisce un vantaggio a chi è più voloce o vice versa.
        double totalDodge = baseDodge + modificatore;

        return Math.max(dodgeMin, Math.min(dodgeCap, totalDodge)); //Garantisce comunque una possibilità minima di schivata in caso di estrema inferiorità di velocita.
    }

    public AttackResult attack (Entity target) {
        double evasione = target.dodgeChance(this); //ottiene la probabilità di schivata dal metodo precedente, valutando target con parametro attacker.

        //valuta se l'attaco è stato evaso o meno
        if (RANDOM.nextDouble() < evasione){
            return new AttackResult(this, target, true, 0, false); //ritorna un oggetto AttackResult con il risultato dell'attacco
        }

        //Sistema di riduzione del danno che garantisce un danno minimo anche con un estrama disparità di difesa.
        double reductionFactor = 100 / (100.0 + target.getDefense());
        int damage = Math.max(1, (int) Math.round(this.attack* reductionFactor));

        target.takeDamage(damage);

        if (!target.isAlive() && this instanceof Esperienza attacker) {
            attacker.gainExp(target.getExp());
        }

        return new AttackResult(this, target, false, damage, !target.isAlive());

    }

    public void takeDamage(int damage) {
        this.stamina -= damage;
        if (this.stamina <= 0) {
            this.stamina = 0;
            this.alive = false;
        }
    }

    public void heal(){
        this.stamina = maxStamina;
    }

    public int getMaxStamina(){return maxStamina;}

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
