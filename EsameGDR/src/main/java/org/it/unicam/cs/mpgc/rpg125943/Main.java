package org.it.unicam.cs.mpgc.rpg125943;


import java.util.List;
import java.util.Scanner;

/**
 * Versione CONSOLE del gioco (senza interfaccia grafica): chiede nome e
 * stile da tastiera, poi fa combattere il player contro tutti i nemici in
 * automatico (tramite {@link GameSession}) fino al boss finale.
 * Implementa {@link BattleEngine.BattleListener} per stampare a schermo
 * cosa succede ad ogni colpo.
 */
public class Main implements BattleEngine.BattleListener {

    private static final Scanner SCANNER = new Scanner(System.in);
    private final BattleEngine battleEngine = new BattleEngine();
    private final GameSession gameSession = new GameSession(battleEngine);

    public static void main(String[] args) {
        new Main().start();
    }

    /**
     * Chiede nome e stile all'utente da tastiera. La lista di stili viene
     * letta da {@code Styles.values()}: aggiungere un nuovo stile in
     * {@code Styles.java} lo fa comparire automaticamente qui come opzione,
     * senza modificare questo metodo.
     *
     * @return il Player creato in base alle scelte dell'utente
     */
    private static Player createPlayer() {
        System.out.println("Come ti chiami?");
        String name = SCANNER.nextLine();

        System.out.println("Scegli il tuo stile di combattimento:");
        Styles[] styles = Styles.values();
        for (int i = 0; i < styles.length; i++) {
            System.out.println((i + 1) + ". " + styles[i]);
        }

        int choice = SCANNER.nextInt();
        SCANNER.nextLine();

        if (choice < 1 || choice > styles.length) {
            throw new IllegalArgumentException("Scelta non valida");
        }

        return Player.of(name, styles[choice - 1]);
    }

    /**
     * Avvia l'intera partita in console: crea il player, lo fa combattere
     * contro tutti i nemici e il boss finale (tramite {@link GameSession}),
     * poi stampa l'esito finale.
     */
    private void start() {
        System.out.println("Bevenuto nella WBC!!!");
        Player player = createPlayer();

        List<Enemy> normalEnemies = GameSession.defaultEnemies();

        boolean won = gameSession.play(
                player,
                normalEnemies,
                this,
                enemy -> System.out.println("Un nuovo nemico e' apparso: " + enemy.getName() + " (Livello: " + enemy.getLevel() + ")"),
                boss -> {
                    System.out.println("Ora dovrai sfidare il campione");
                    System.out.println("Il campione e': " + boss.getName());
                }
        );


        if (won) {
            System.out.println("Congratulazioni sei il nuovo Campione del mondo!");
        } else {
            System.out.println("Game over");
        }
    }

    @Override
    public void onAttack(AttackResult result) {
        if (result.isDodged()) {
            System.out.println(result.getTarget().getName() + " Ha schivato l'attacco di " + result.getAttacker().getName());
        } else {
            System.out.println(result.getAttacker().getName() + " ha attaccato " + result.getTarget().getName() + " e ha inflitto " + result.getDamage() + " danni.");
        }
    }

    @Override
    public void onBattleEnd(Entity winner, Entity loser) {
        System.out.println(loser.getName() + " e' stato sconfitto da " + winner.getName() + "!");
    }
}