package org.it.unicam.cs.mpgc.rpg125943.ui;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.Button;
import javafx.geometry.Insets;
import org.it.unicam.cs.mpgc.rpg125943.Enemy;
import java.util.List;
import org.it.unicam.cs.mpgc.rpg125943.Player;
import org.it.unicam.cs.mpgc.rpg125943.Entity;
import org.it.unicam.cs.mpgc.rpg125943.AttackResult;
import org.it.unicam.cs.mpgc.rpg125943.BattleEngine;


public class App extends Application implements BattleEngine.BattleListener {

    private TextArea ring = new TextArea();
    private Label statusLabel = new Label("Premi 'Inizia' per giocare");
    private BattleEngine battleEngine = new BattleEngine();
    Player player;

    public void start(Stage stage) {
        ring.setEditable(false);
        ring.setPrefHeight(300);

        Button startButton = new Button("Inizia");
        startButton.setOnAction(e -> startGame());

        VBox root = new VBox(10, statusLabel, ring, startButton);
        root.setPadding(new Insets(20));

        stage.setScene(new Scene(root, 500, 450));
        stage.setTitle("Benvenuto nella WBC!!!");
        stage.show();
    }

    private void startGame() {
        ring.clear();
        player = Player.brawler("prova");

        List<Enemy> enemies = List.of(
                Enemy.nemicoCasuale("Vincenzo", 1),
                Enemy.nemicoCasuale("Glad0s", 1),
                Enemy.nemicoCasuale("Soap", 1),
                Enemy.nemicoCasuale("Boros", 2),
                Enemy.nemicoCasuale("Looter", 2),
                Enemy.nemicoCasuale("Pasta", 2),
                Enemy.nemicoCasuale("Dogmeat", 3),
                Enemy.nemicoCasuale("Vladimir", 3),
                Enemy.nemicoCasuale("Mob", 3),
                Enemy.nemicoCasuale("Freeman", 4)
        );

        for (Enemy enemy : enemies) {
            if (!player.isAlive()) break;
            ring.appendText("Inizia la battaglia contro " + enemy.getName() + "!\n");
            battleEngine.runBattle(player, enemy, this);
            if (player.isAlive()) {
                player.heal();
            }
        }

        statusLabel.setText(player.isAlive() ? "Hai vinto!" : "Game over!");
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
    public void onBattleEnd(Entity winner, Entity loser){
        ring.appendText(loser.getName() + " è stato sconfitto da " + winner.getName() + "!\n" );
    }

    public static void main (String[]args){
        launch(args);
    }
}