package org.it.unicam.cs.mpgc.rpg125943;

public class Player extends Entity implements Esperienza {

    private Styles style;
    private int incrementalExp = 30;


    protected Player(String name, int stamina, int maxStamina , int attack, int defense, int speed, int level, double exp, boolean alive, Styles style) {
        super(name, stamina, maxStamina , attack, defense, speed, level, exp, alive);
        this.style = style;
    }

    public static Player brawler(String name) {
        return new Player(name, 150, 150 , 8, 10, 5, 1, 0.0, true, Styles.BRAWLER);
    }


    public static Player inFighter(String name) {
        return new Player(name, 100, 100, 20, 5, 8, 1, 0.0, true, Styles.IN_FIGHTER);
    }


    public static Player defenseLab(String name) {
        return new Player(name, 100, 100 , 5, 40, 12, 1, 0.0, true, Styles.DEFENSE_LAB);
    }


    public static Player outBoxer(String name) {
        return new Player(name, 50, 50 , 10, 8, 20, 1, 0.0, true, Styles.OUT_BOXER);
    }


    @Override
    public void gainExp(double exp) {
        this.exp += exp;
        if (this.exp >= incrementalExp) {
            this.levelUp();
            this.exp = 0;
            incrementalExp = (int) (incrementalExp * 1.2);
        }
    }

    //Grazie a questo metodo ogni classe avrà un diverso LVL UP che migliorerà le sue statistiche di punta.
    private void levelUp() {
        this.level++;
        switch (style){
            case BRAWLER:
                this.maxStamina += 25;
                this.attack += 3;
                this.defense += 2;
                this.speed += 2;
                break;
            case IN_FIGHTER:
                this.maxStamina += 10;
                this.attack += 8;
                this.defense += 1;
                this.speed += 2;
                break;
            case DEFENSE_LAB:
                this.maxStamina += 5;
                this.attack += 1;
                this.defense += 10;
                this.speed += 3;
                break;
            case OUT_BOXER:
                this.maxStamina += 5;
                this.attack += 1;
                this.defense += 1;
                this.speed += 4;
                break;
            default:
                break;
        }
    }

    public Styles getStyle(){
        return style;
    }

}
