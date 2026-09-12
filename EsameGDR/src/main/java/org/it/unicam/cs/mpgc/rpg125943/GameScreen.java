package org.it.unicam.cs.mpgc.rpg125943;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Schermata di gioco vera e propria.
 * <p>
 * Riceve {@link BattleEngine} e {@link SaveManager} dall'esterno invece di crearseli da solo con "new" al proprio interno,
 * questa classe dipende solo dalle astrazioni che le vengono passate senza decidere lei
 * come vengono costruite le sue dipendenze.
 */
public class GameScreen extends VBox implements BattleEngine.BattleListener {

    private final TextArea ring = new TextArea();
    private final Label statusLabel = new Label("Inserisci nome e stile poi premi 'Inizia'");
    private final TextField nameField = new TextField();
    private final ComboBox<Styles> styleBox = new ComboBox<>();
    private final Button startButton = new Button("Inizia");
    private final Button attackButton = new Button("Attacca");
    private final Button saveButton = new Button("Salva");
    private final Button menuButton = new Button("Torna al menu");

    private final Label playerStatsLabel = new Label("-");
    private final Label enemyStatsLabel = new Label("-");

    private final BattleEngine battleEngine;
    private final SaveManager saveManager;

    private Player player;
    private Turns session;

    private Runnable onBackToMenu;

    /**
     * @param battleEngine motore di combattimento usato per gli scambi turno per turno
     * @param saveManager  gestore della persistenza usato per salvare/caricare le partite
     */
    public GameScreen(BattleEngine battleEngine, SaveManager saveManager) {
        this.battleEngine = battleEngine;
        this.saveManager = saveManager;

        ring.setEditable(false);
        ring.setPrefHeight(300);

        nameField.setPromptText("Nome del personaggio");
        styleBox.getItems().addAll(Styles.values());
        styleBox.setValue(Styles.BRAWLER);

        attackButton.setDisable(true);
        saveButton.setDisable(true);
        menuButton.setDisable(true);

        startButton.setOnAction(e -> startGame());
        attackButton.setOnAction(e -> attack());
        saveButton.setOnAction(e -> saveGame());
        menuButton.setOnAction(e -> {
            player = null;
            session = null;
            if (onBackToMenu != null) {
                onBackToMenu.run();
            }
        });

        HBox setup = new HBox(10, nameField, styleBox, startButton);
        HBox battleControls = new HBox(10, attackButton, saveButton, menuButton);

        VBox playerStatsBox = new VBox(5, new Label("Il tuo personaggio"), playerStatsLabel);
        VBox enemyStatsBox = new VBox(5, new Label("Avversario attuale"), enemyStatsLabel);
        HBox statsBox = new HBox(40, playerStatsBox, enemyStatsBox);

        getChildren().addAll(statusLabel, setup, battleControls, statsBox, ring);
        setSpacing(10);
        setPadding(new Insets(20));
    }

    /**
     * Registra cosa fare quando l'utente preme "Torna al menu": chi usa
     * questa classe ({@code App}) decide quale schermata mostrare al posto
     * di questa.
     *
     * @param onBackToMenu azione da eseguire al ritorno al menu
     */
    public void setOnBackToMenu(Runnable onBackToMenu) {
        this.onBackToMenu = onBackToMenu;
    }

    /** Riporta la schermata allo stato "pronta per una nuova partita": campi vuoti e riabilitati. */
    public void prepareNewGame() {
        player = null;
        session = null;

        ring.clear();
        nameField.clear();
        nameField.setDisable(false);
        styleBox.setValue(Styles.BRAWLER);
        styleBox.setDisable(false);
        startButton.setDisable(false);

        attackButton.setDisable(true);
        saveButton.setDisable(true);
        menuButton.setDisable(true);

        playerStatsLabel.setText("-");
        enemyStatsLabel.setText("-");

        statusLabel.setText("Inserisci nome e stile, poi premi 'Inizia'");
    }

    /** Crea il Player scelto dall'utente (nome + stile) e avvia una nuova sessione {@link Turns}. */
    private void startGame() {
        ring.clear();
        player = Player.of(readName(), readStyle());

        List<Enemy> enemies = GameSession.defaultEnemies();

        session = new Turns(
                battleEngine,
                player,
                enemies,
                this,
                enemy -> ring.appendText("Un nuovo nemico e' apparso: " + enemy.getName() + " (Livello: " + enemy.getLevel() + ")\n"),
                boss -> ring.appendText("Ora dovrai sfidare il campione: " + boss.getName() + "\n")
        );

        nameField.setDisable(true);
        styleBox.setDisable(true);
        startButton.setDisable(true);
        attackButton.setDisable(false);
        saveButton.setDisable(false);
        menuButton.setDisable(false);

        statusLabel.setText("Inizia la battaglia! Premi 'Attacca' per combattere.");
        refreshStats();
    }

    /**
     * Carica una partita salvata e ricostruisce la sessione esattamente al
     * punto in cui era stata salvata (stesso nemico/boss, stessa stamina
     * residua).
     *
     * @param saveName nome del salvataggio da caricare
     * @return true se il caricamento e' andato a buon fine, false altrimenti
     */
    public boolean loadGame(String saveName){
        GameSaveData data;
        try {
            data = saveManager.load(saveName);
        } catch (RuntimeException ex) {
            ring.appendText("Caricamento fallito" + ex.getMessage() + "\n");
            return false;
        }

        ring.clear();
        player = data.getPlayer().toPlayer();

        List<Enemy> enemies = new ArrayList<>();
        for (EnemyData enemyData : data.getEnemies()) {
            enemies.add(enemyData.toEnemy());
        }

        Entity resumedOpponent = (data.getEnemyIndex() < enemies.size())
                ? enemies.get(data.getEnemyIndex())
                : (data.getBoss() != null ? data.getBoss().toEnemy() : null);

        session = new Turns(
                battleEngine,
                player,
                enemies,
                data.getEnemyIndex(),
                resumedOpponent,
                this,
                enemy -> ring.appendText("Un nuovo nemico e' apparso: " + enemy.getName() + " (Livello: " + enemy.getLevel() + ")\n"),
                boss -> ring.appendText("Ora dovrai sfidare il campione: " + boss.getName() + "\n")
        );

        nameField.setDisable(true);
        styleBox.setDisable(true);
        startButton.setDisable(true);
        attackButton.setDisable(false);
        saveButton.setDisable(false);
        menuButton.setDisable(false);

        ring.appendText("Partita caricata: " + player.getName() + " (nemici superati finora: " + data.getEnemyIndex() + ")\n");
        statusLabel.setText("Partita caricata! Premi 'Attacca' per continuare.");
        refreshStats();

        return true;
    }

    /**
     * Chiede il nome del salvataggio (finestrina JavaFX) e scrive su disco
     * il personaggio + lo stato esatto di tutti i nemici + eventuale boss.
     * Se l'utente annulla la finestrina, non salva nulla.
     */
    private void saveGame() {
        if (player == null || session == null) {
            return;
        }

        TextInputDialog dialog = new TextInputDialog(player.getName());
        dialog.setTitle("Salva partita");
        dialog.setHeaderText(null);
        dialog.setContentText("Nome del salvataggio:");
        Optional<String> result = dialog.showAndWait();

        if (result.isEmpty() || result.get().isBlank()) {
            return; // l'utente ha annullato: non si salva nulla
        }
        String saveName = result.get().trim();

        try {
            List<EnemyData> enemiesData = new ArrayList<>();
            for (Enemy enemy : session.getEnemies()) {
                enemiesData.add(EnemyData.fromEntity(enemy));
            }

            // Il boss non fa parte della lista "enemies": va salvato a parte,
            // solo se lo si stava effettivamente affrontando.
            EnemyData bossData = session.isFightingBoss()
                    ? EnemyData.fromEntity(session.getCurrentOpponent())
                    : null;

            saveManager.save(saveName, player, enemiesData, session.getEnemyIndex(), session.isFightingBoss(), bossData);
            ring.appendText("Partita salvata come '" + saveName + "'.\n");
        } catch (RuntimeException ex) {
            ring.appendText("Salvataggio fallito: " + ex.getMessage() + "\n");
        }
    }

    /** Chiamato dal bottone "Attacca": esegue un turno e aggiorna la UI. */
    private void attack() {
        if (session == null) {
            return;
        }

        session.attack();
        refreshStats();

        if (session.isFinished()) {
            attackButton.setDisable(true);
            saveButton.setDisable(true);
            statusLabel.setText(session.hasWon() ? "Hai vinto la battaglia!" : "Hai perso la battaglia!");
        }
    }

    /** Aggiorna i due pannelli di statistiche (player + avversario attuale). */
    private void refreshStats() {
        if (player != null) {
            playerStatsLabel.setText(formatStats(player));
        }

        Entity opponent = (session != null ? session.getCurrentOpponent() : null);
        enemyStatsLabel.setText(opponent != null ? formatStats(opponent) : "Nessun avversario");
    }

    /**
     * Testo da mostrare per le statistiche di un'entita' qualsiasi (player o nemico).
     *
     * @param entity l'entita' di cui mostrare le statistiche
     * @return il testo multilinea con nome, stamina, attacco, difesa, velocita' e livello
     */
    private String formatStats(Entity entity) {
        return entity.getName() + "\n"
                + "Stamina: " + entity.getStamina() + "/" + entity.getMaxStamina() + "\n"
                + "Attacco: " + entity.getAttack() + "\n"
                + "Difesa: " + entity.getDefense() + "\n"
                + "Velocita': " + entity.getSpeed() + "\n"
                + "Livello: " + entity.getLevel();
    }

    private String readName() {
        String rawName = nameField.getText();
        return (rawName == null || rawName.isBlank()) ? "Sfidante" : rawName.trim();
    }

    private Styles readStyle() {
        return styleBox.getValue() == null ? Styles.BRAWLER : styleBox.getValue();
    }

    /** Chiamato dal BattleEngine dopo ogni attacco: scrive nel log della battaglia. */
    @Override
    public void onAttack(AttackResult result) {
        if (result.isDodged()) {
            ring.appendText(result.getTarget().getName() + " ha schivato l'attacco di " + result.getAttacker().getName() + "!\n");
        } else {
            ring.appendText(result.getAttacker().getName() + " ha attaccato " + result.getTarget().getName() + " e ha inflitto " + result.getDamage() + " danni.\n");
        }
    }

    /** Chiamato dal BattleEngine quando uno scontro finisce (uno dei due muore). */
    @Override
    public void onBattleEnd(Entity winner, Entity loser) {
        ring.appendText(loser.getName() + " e' stato sconfitto da " + winner.getName() + "!\n");
    }

}
