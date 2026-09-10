package org.it.unicam.cs.mpgc.rpg125943.ui;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Button;
import javafx.geometry.Insets;

import org.it.unicam.cs.mpgc.rpg125943.*;

import java.util.List;


public class App extends Application implements BattleEngine.BattleListener {

    private final TextArea ring = new TextArea();
    private final Label statusLabel = new Label("Inserisci nome e stile, poi premi 'Inizia'");
    private final TextField nameField = new TextField();
    private final ComboBox<Styles> styleBox = new ComboBox<>();
    private final Button startButton = new Button("Inizia");
    private final Button attackButton = new Button("Attacca");
    private final Button restartButton = new Button("Ricomincia");

    private final Label playerStatsLabel = new Label("-");
    private final Label enemyStatsLabel = new Label("-");

    private final BattleEngine battleEngine = new BattleEngine();

    private Player player;
    private Turns session;


    @Override
    public void start(Stage stage) {
        ring.setEditable(false);
        ring.setPrefHeight(320);

        nameField.setPromptText("Nome del personaggio");

        styleBox.getItems().addAll(Styles.values());
        styleBox.setValue(Styles.BRAWLER);

        attackButton.setDisable(true);
        restartButton.setDisable(true);

        startButton.setOnAction(e -> startGame());
        attackButton.setOnAction(e -> attack());
        restartButton.setOnAction(e -> restart());

        HBox setup = new HBox(10, nameField, styleBox, startButton);
        HBox battleControls = new HBox(10, attackButton, restartButton);

        VBox playerStatsBox = new VBox(5, new Label("Il tuo personaggio"), playerStatsLabel);
        VBox enemyStatsBox = new VBox(5, new Label("Avversario attuale"), enemyStatsLabel);
        HBox statsBox = new HBox(40, playerStatsBox, enemyStatsBox);


        VBox root = new VBox(10, statusLabel, setup, battleControls, statsBox, ring);
        root.setPadding(new Insets(20));

        stage.setScene(new Scene(root, 750, 600));
        stage.setTitle("Benvenuto nella WBC!!!");
        stage.show();
    }

    private void startGame() {
        ring.clear();
        player = createPlayer();

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

        statusLabel.setText("Inizia la battaglia! Premi 'Attacca' per combattere.");
        refreshStats();

    }

    private void refreshStats() {
        if (player != null){
            playerStatsLabel.setText(formatStats(player));
        }

        Entity opponent = (session != null ? session.getCurrentOpponent() : null);
        enemyStatsLabel.setText(opponent != null ? formatStats(opponent) : "Nessun avversario");
    }

    private String formatStats(Entity entity) {
        return entity.getName() + "\n"
                + "Stamina: " + entity.getStamina() + "/" + entity.getMaxStamina() + "\n"
                + "Attacco: " + entity.getAttack() + "\n"
                + "Difesa: " + entity.getDefense() + "\n"
                + "Velocita': " + entity.getSpeed() + "\n"
                + "Livello: " + entity.getLevel();
    }

    private void attack(){
        if (session == null){
            return;
        }

        session.attack();
        refreshStats();

        if (session.isFinished()){
            attackButton.setDisable(true);
            restartButton.setDisable(false);
            statusLabel.setText(session.hasWon() ? "Hai vinto la battaglia!" : "Hai perso la battaglia!");
        }
    }

    private void restart() {
        player = null;
        session = null;

        ring.clear();

        nameField.clear();
        nameField.setDisable(false);
        styleBox.setValue(Styles.BRAWLER);
        styleBox.setDisable(false);
        startButton.setDisable(false);

        attackButton.setDisable(true);
        restartButton.setDisable(true);

        playerStatsLabel.setText("-");
        enemyStatsLabel.setText("-");

        statusLabel.setText("Inserisci nome e stile, poi premi 'Inizia'");
    }


    private Player createPlayer() {
        String rawName = nameField.getText();
        String name = (rawName == null || rawName.isBlank()) ? "Sfidante" : rawName.trim();
        Styles style = styleBox.getValue() == null ? Styles.BRAWLER : styleBox.getValue();

        return switch (style) {
            case BRAWLER -> Player.brawler(name);
            case IN_FIGHTER -> Player.inFighter(name);
            case DEFENSE_LAB -> Player.defenseLab(name);
            case OUT_BOXER -> Player.outBoxer(name);
        };
    }

    @Override
    public void onAttack(AttackResult result) {
        if (result.isDodged()) {
            ring.appendText(result.getTarget().getName() + " ha schivato l'attacco di " + result.getAttacker().getName() + "!\n");
        } else {
            ring.appendText(result.getAttacker().getName() + " ha attaccato " + result.getTarget().getName() + " e ha inflitto " + result.getDamage() + " danni.\n");
        }
    }

    @Override
    public void onBattleEnd(Entity winner, Entity loser) {
        ring.appendText(loser.getName() + " e' stato sconfitto da " + winner.getName() + "!\n");
    }

    public static void main(String[] args) {
        launch(args);
    }
}