package org.it.unicam.cs.mpgc.rpg125943;


/**
 * Il personaggio controllato dall'utente. Estende {@link Entity} (ha le
 * stesse statistiche base) e implementa {@link Esperienza} perche' e'
 * l'unico che sale di livello guadagnando exp dai nemici sconfitti.
 */
public class Player extends Entity implements Esperienza {

    private Styles style;
    private int incrementalExp = 30; // quanta exp serve per il prossimo livello (cresce ogni volta)

    /**
     * Costruttore protected: usato dal factory method {@link #of(String, Styles)}
     * e da {@code PlayerData.toPlayer()} (stesso package) per ricostruire un
     * player salvato con le sue statistiche esatte.
     */
    protected Player(String name, int stamina, int maxStamina , int attack, int defense, int speed, int level, double exp, boolean alive, Styles style) {
        super(name, stamina, maxStamina , attack, defense, speed, level, exp, alive);
        this.style = style;
    }

    /**
     * Crea un nuovo Player nello stile scelto, con le statistiche di
     * partenza definite in {@link Styles}. Un solo factory method per tutti
     * gli stili: prima ce n'era uno per stile ({@code brawler()},
     * {@code inFighter()}, ecc.), ora le differenze tra stili vivono in
     * {@code Styles}, non qui.
     *
     * @param name  nome del personaggio
     * @param style stile di combattimento scelto
     * @return un nuovo Player di livello 1 con le statistiche base dello stile
     */

    public static Player of(String name, Styles style) {
        return new Player(name, style.getBaseStamina(), style.getBaseStamina(), style.getBaseAttack(), style.getBaseDefense(), style.getBaseSpeed(), 1, 0.0, true, style);
    }


    /**
     * Chiamato da {@link Entity#attack(Entity)} quando questo player
     * sconfigge qualcuno. Se l'exp accumulata basta, sale di livello e la
     * soglia per il prossimo livello aumenta un po' (+20%), cosi' salire
     * diventa via via piu' lento.
     *
     * @param exp quantita' di esperienza guadagnata dall'ultimo avversario sconfitto
     */
    @Override
    public void gainExp(double exp) {
        this.exp += exp;
        while (this.exp >= incrementalExp) {
            levelUp();
            this.exp = 0;
            incrementalExp *= 1.2; // aumenta la quantità di exp necessaria per il prossimo livello
        }
    }

    /**
     * Applica la crescita di livello. Prima c'era uno switch sui 4 stili
     * qui dentro: ora ogni stile porta gia' con se' i propri incrementi
     * (vedi {@link Styles}), quindi aggiungere un nuovo stile non tocca
     * questo metodo.
     */
    private void levelUp() {
        this.level++;
        this.maxStamina += style.getStaminaGrowth();
        this.attack += style.getAttackGrowth();
        this.defense += style.getDefenseGrowth();
        this.speed += style.getSpeedGrowth();
    }

    public Styles getStyle() {
        return style;
    }
}

