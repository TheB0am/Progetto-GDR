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

import org.it.unicam.cs.mpgc.rpg125943.Enemy;
import org.it.unicam.cs.mpgc.rpg125943.Player;
import org.it.unicam.cs.mpgc.rpg125943.Entity;
import org.it.unicam.cs.mpgc.rpg125943.AttackResult;
import org.it.unicam.cs.mpgc.rpg125943.BattleEngine;
import org.it.unicam.cs.mpgc.rpg125943.GameSession;
import org.it.unicam.cs.mpgc.rpg125943.Styles;

import java.util.List;


public class App extends Application implements BattleEngine.BattleListener {

    private final TextArea ring = new TextArea();
    private final Label statusLabel = new Label("Inserisci nome e stile, poi premi 'Inizia'");
    private final TextField nameField = new TextField();
    private final ComboBox<Styles> styleBox = new ComboBox<>();
    private final Button startButton = new Button("Inizia");

    private final BattleEngine battleEngine = new BattleEngine();
    private final GameSession gameSession = new GameSession(battleEngine);

    private Player player;

    @Override
    public void start(Stage stage) {
        ring.setEditable(false);
        ring.setPrefHeight(300);

        nameField.setPromptText("Nome del personaggio");

        styleBox.getItems().addAll(Styles.values());
        styleBox.setValue(Styles.BRAWLER);

        startButton.setOnAction(e -> startGame());

        HBox setup = new HBox(10, nameField, styleBox, startButton);
        VBox root = new VBox(10, statusLabel, setup, ring);
        root.setPadding(new Insets(20));

        stage.setScene(new Scene(root, 550, 450));
        stage.setTitle("Benvenuto nella WBC!!!");
        stage.show();
    }

    private void startGame() {
        ring.clear();
        player = createPlayer();

        List<Enemy> enemies = GameSession.defaultEnemies();

        boolean won = gameSession.play(
                player,
                enemies,
                this,
                enemy -> ring.appendText("Un nuovo nemico e' apparso: " + enemy.getName() + " (Livello: " + enemy.getLevel() + ")\n"),
                boss -> ring.appendText("Ora dovrai sfidare il campione: " + boss.getName() + "\n")
        );

        statusLabel.setText(won ? "Hai vinto! Sei il nuovo Campione del mondo!" : "Game over!");
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