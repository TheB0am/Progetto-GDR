package org.it.unicam.cs.mpgc.rpg125943.ui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.collections.ListChangeListener;

import org.it.unicam.cs.mpgc.rpg125943.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class App extends Application implements BattleEngine.BattleListener {

    //Menu principale
    private final Button newGameButton = new Button("Nuova Partita");
    private final Button loadGameButton = new Button("Carica Partita");
    private final Button exitButton = new Button("Esci");
    private VBox menuRoot;

    //Schermata Salvataggi
    private final ListView<String> savesListView = new ListView<>();
    private final Button loadSelectedButton = new Button("Carica");
    private final Button deleteSelectedButton = new Button("Elimina");
    private final Button backFromLoadButton = new Button("Indietro");
    private VBox loadRoot;

    //Schermata di gioco
    private final TextArea ring = new TextArea();
    private final Label statusLabel = new Label("Inserisci nome e stile, poi premi 'Inizia'");
    private final TextField nameField = new TextField();
    private final ComboBox<Styles> styleBox = new ComboBox<>();
    private final Button startButton = new Button("Inizia");
    private final Button attackButton = new Button("Attacca");
    private final Button saveButton = new Button("Salva");
    private final Button restartButton = new Button("Ricomincia");
    private VBox gameRoot;

    //Display statistiche
    private final Label playerStatsLabel = new Label("-");
    private final Label enemyStatsLabel = new Label("-");

    private final BattleEngine battleEngine = new BattleEngine();
    private final SaveManager saveManager = new SaveManager();

    private Stage stage;
    private Player player;
    private Turns session;


    @Override
    public void start(Stage stage) {
        this.stage = stage;

        buildMenu ();
        buildLoadScreen();
        buildGameScreen();
        refreshMenuButtons();

        stage.setScene(new Scene(menuRoot, 750, 600));
        stage.setTitle("Benvenuto nella WBC!!!");
        stage.show();
    }

    private void buildMenu(){
        Label title = new Label("WBC - World Boxing Championship");

        newGameButton.setOnAction(e -> {
           prepareNewGame();
           stage.getScene().setRoot(gameRoot);
        });

        loadGameButton.setOnAction(e -> {
           refreshSaveList();
           stage.getScene().setRoot(loadRoot);
        });

        exitButton.setOnAction(e -> Platform.exit());

        menuRoot = new VBox(20, title, newGameButton, loadGameButton, exitButton);
        menuRoot.setAlignment(Pos.CENTER);
        menuRoot.setPadding(new Insets(40));
    }

    private void buildLoadScreen(){
        savesListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        loadSelectedButton.setDisable(true);
        deleteSelectedButton.setDisable(true);

        savesListView.getSelectionModel().getSelectedItems().addListener((ListChangeListener<String>) change -> {
            int selectedCount = savesListView.getSelectionModel().getSelectedItems().size();
            loadSelectedButton.setDisable(selectedCount != 1);
            deleteSelectedButton.setDisable(selectedCount == 0);
        });

        loadSelectedButton.setOnAction(e -> {
           List<String> selected = new ArrayList<>(savesListView.getSelectionModel().getSelectedItems());
           if(selected.size() == 1 && loadGame(selected.get(0))){
               stage.getScene().setRoot(gameRoot);
           }
        });

        deleteSelectedButton.setOnAction(e -> {
            List<String> selected = new ArrayList<>(savesListView.getSelectionModel().getSelectedItems());
            if (selected.isEmpty()) return;

            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                    "Eliminare " + selected.size() + " salvataggio/i selezionato/i? L'operazione non si puo' annullare.",
                    ButtonType.YES, ButtonType.NO);
            confirm.setHeaderText(null);
            confirm.setTitle("Conferma eliminazione");

            Optional<ButtonType> answer = confirm.showAndWait();
            if (answer.isPresent() && answer.get() == ButtonType.YES) {
                for (String saveName : selected) {
                    saveManager.delete(saveName);
                }
                refreshSaveList();
            }
        });

        backFromLoadButton.setOnAction(e -> {
            refreshMenuButtons();
            stage.getScene().setRoot(menuRoot);
        });

        HBox loadControls = new HBox(10, loadSelectedButton, deleteSelectedButton, backFromLoadButton);
        loadRoot = new VBox(15, new Label("Salvataggi disponibili"), savesListView, loadControls);
        loadRoot.setPadding(new Insets(20));
    }

    private void refreshSaveList(){
        savesListView.getItems().setAll(saveManager.listSaves());
    }

    private void buildGameScreen(){
        ring.setEditable(false);
        ring.setPrefHeight(300);

        nameField.setPromptText("Nome del personaggio");

        styleBox.getItems().addAll(Styles.values());
        styleBox.setValue(Styles.BRAWLER);

        attackButton.setDisable(true);
        saveButton.setDisable(true);
        restartButton.setDisable(true);

        startButton.setOnAction(e -> startGame());
        attackButton.setOnAction(e -> attack());
        saveButton.setOnAction(e -> saveGame());
        restartButton.setOnAction(e -> backToMenu());

        HBox setup = new HBox(10, nameField, styleBox, startButton);
        HBox battleControls = new HBox(10, attackButton, saveButton, restartButton);

        VBox playerStatsBox = new VBox(5, new Label("Il tuo personaggio"), playerStatsLabel);
        VBox enemyStatsBox = new VBox(5, new Label("Avversario attuale"), enemyStatsLabel);
        HBox statsBox = new HBox(40, playerStatsBox, enemyStatsBox);

        gameRoot = new VBox(10, statusLabel, setup, battleControls, statsBox, ring);
        gameRoot.setPadding(new Insets(20));
    }

    private void prepareNewGame(){
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
        restartButton.setDisable(true);

        playerStatsLabel.setText("-");
        enemyStatsLabel.setText("-");

        statusLabel.setText("Inserisci nome e stile, poi premi 'Inizia'");
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
        saveButton.setDisable(false);
        restartButton.setDisable(false);

        statusLabel.setText("Inizia la battaglia! Premi 'Attacca' per combattere.");
        refreshStats();
    }

    private boolean loadGame(String saveName){
        GameSaveData data;
        try {
            data = saveManager.load(saveName);
        } catch (RuntimeException ex) {
            ring.appendText("Caricamento fallito: " + ex.getMessage() + "\n");
            return false;
        }

        ring.clear();
        player = data.getPlayer().toPlayer();

        List<Enemy> enemies = new ArrayList<>();
        for (EnemyData enemyData : data.getEnemies()){
            enemies.add(enemyData.toEnemy());
        }

        Entity resumedOpponent = (data.getEnemyIndex() < enemies.size()) ? enemies.get(data.getEnemyIndex()) : (data.getBoss() != null ? data.getBoss().toEnemy() : null);

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
        restartButton.setDisable(false);

        ring.appendText("Partita caricata: " + player.getName() + " (nemici superati finora: " + data.getEnemyIndex() + ")\n");
        statusLabel.setText("Partita caricata! Premi 'Attacca' per continuare.");
        refreshStats();
        return true;
    }

    private void saveGame(){
        if (player == null || session == null) {
            return;
        }

        TextInputDialog dialog = new TextInputDialog(player.getName());
        dialog.setTitle("Salva partita");
        dialog.setHeaderText(null);
        dialog.setContentText("Nome del salvataggio:");
        Optional<String> result = dialog.showAndWait();

        if (result.isEmpty() || result.get().isBlank()){
            return;
        }
        String saveName = result.get().trim();

        try {
            List<EnemyData> enemiesData = new ArrayList<>();
            for (Enemy enemy : session.getEnemies()) {
                enemiesData.add(EnemyData.fromEntity(enemy));
            }

            EnemyData bossData = session.isFightingBoss() ? EnemyData.fromEntity(session.getCurrentOpponent()) : null;

            saveManager.save(saveName, player, enemiesData, session.getEnemyIndex(), session.isFightingBoss(), bossData);
            ring.appendText("Partita salvata come '" + saveName + "'.\n");
        } catch (RuntimeException ex) {
            ring.appendText("Salvataggio fallito: " + ex.getMessage() + "\n");
        }
    }


    private void attack(){
        if (session == null){
            return;
        }

        session.attack();
        refreshStats();

        if (session.isFinished()){
            attackButton.setDisable(true);
            saveButton.setDisable(true);
            statusLabel.setText(session.hasWon() ? "Hai vinto la battaglia!" : "Hai perso la battaglia!");
        }
    }

    private void backToMenu(){
        player = null;
        session = null;
        refreshMenuButtons();
        stage.getScene().setRoot(menuRoot);
    }

    private void refreshMenuButtons(){
        loadGameButton.setDisable(!saveManager.hasAnySave());
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