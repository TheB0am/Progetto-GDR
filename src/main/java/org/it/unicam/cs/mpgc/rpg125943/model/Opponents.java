package org.it.unicam.cs.mpgc.rpg125943.model;

import java.util.List;

/**
 * Contenuto della campagna: chi sono gli avversari che si incontrano
 * (nemici normali e boss).
 * <p>
 * Vive nel package "model" apposta: "chi sono gli avversari" e' un
 * concetto di dominio, non logica di una particolare modalita' di gioco.
 * Sia {@code GameSession} che {@code Turns} lo usano allo stesso modo, senza che una dipenda dall'altra.
 */
public class Opponents {

    private Opponents() {
        // solo metodi statici, non ha senso creare un'istanza
    }

    /**
     * La lista standard di nemici della campagna. Attenzione: ogni chiamata
     * genera nemici NUOVI con statistiche casuali diverse (vedi
     * {@link Enemy#nemicoCasuale(String, int)}), non sono sempre gli stessi.
     *
     * @return una nuova lista di nemici con livelli crescenti
     */
    public static List<Enemy> defaultEnemies() {
        return List.of(
                Enemy.nemicoCasuale("Ciotta", 1),
                Enemy.nemicoCasuale("Glad0s", 1),
                Enemy.nemicoCasuale("JoJo", 1),
                Enemy.nemicoCasuale("Krilin", 1),
                Enemy.nemicoCasuale("Volg", 2),
                Enemy.nemicoCasuale("Geralt", 2),
                Enemy.nemicoCasuale("Connor", 2),
                Enemy.nemicoCasuale("Dogmeat", 3),
                Enemy.nemicoCasuale("Rayman", 3),
                Enemy.nemicoCasuale("Mob", 3),
                Enemy.nemicoCasuale("Doakes", 4),
                Enemy.nemicoCasuale("Mike Ehrmantraut", 4),
                Enemy.nemicoCasuale("Kimball Cho", 4),
                Enemy.nemicoCasuale("Dexter", 5)
        );
    }

    /**
     * Sceglie un boss a caso tra i 4 disponibili. Usato sia dalla modalita'
     * automatica che da quella a turni, cosi' aggiungere un quinto boss
     * significa modificare un solo posto.
     *
     * @return un boss scelto casualmente tra quelli disponibili
     */
    public static Boss randomBoss() {
        Boss[] bosses = { Boss.bigBoss(), Boss.Joe(), Boss.vas(), Boss.dutch() };
        return bosses[(int) (Math.random() * bosses.length)];
    }

}
