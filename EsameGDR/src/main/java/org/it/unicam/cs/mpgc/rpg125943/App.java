package org.it.unicam.cs.mpgc.rpg125943;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

/**
 * Punto di ingresso dell'applicazione JavaFX.
 * <p>
 * PRIMA questa classe costruiva tutte e tre le schermate, teneva tutti i
 * campi della UI e conteneva anche la logica di gioco
 * creare il player, gestire i turni, salvare/caricare.
 * <p>
 * ORA il suo unico compito e' costruire le tre schermate ({@link MenuScreen},
 * {@link SaveListScreen}, {@link GameScreen}) e collegarle tra loro: quando
 * una schermata segnala un'azione (es. "premuto Nuova Partita"), App decide
 * quale altra schermata mostrare. Non contiene piu' nessun bottone, nessuna
 * label, nessuna logica di combattimento o di salvataggio - quella vive
 * nelle tre classi dedicate, ognuna con una singola responsabilita'.
 */
public class App extends Application {

    private final BattleEngine battleEngine = new BattleEngine();
    private final SaveManager saveManager = new SaveManager();

    private Stage stage;
    private MenuScreen menuScreen;
    private SaveListScreen saveListScreen;
    private GameScreen gameScreen;

    /** Punto di ingresso JavaFX: costruisce le tre schermate e mostra il menu. */
    @Override
    public void start(Stage stage) {
        this.stage = stage;

        menuScreen = new MenuScreen();
        saveListScreen = new SaveListScreen();
        gameScreen = new GameScreen(battleEngine, saveManager);

        wireNavigation();
        refreshMenu();

        stage.setScene(new Scene(menuScreen, 750, 600));
        stage.setTitle("Benvenuto nella WBC!!!");
        stage.show();
    }

    /**
     * Collega le azioni delle varie schermate alla navigazione tra le scene.
     * Questo e' l'unico posto dove "chi fa cosa" viene deciso.
     */
    private void wireNavigation() {
        menuScreen.setOnNewGame(() -> {
            gameScreen.prepareNewGame();
            showScreen(gameScreen);
        });

        menuScreen.setOnLoadGame(() -> {
            saveListScreen.showSaves(saveManager.listSaves());
            showScreen(saveListScreen);
        });

        menuScreen.setOnExit(Platform::exit);

        saveListScreen.setOnLoad(saveName -> {
            if (gameScreen.loadGame(saveName)) {
                showScreen(gameScreen);
            }
        });

        saveListScreen.setOnDelete(saveNames -> {
            for (String saveName : saveNames) {
                saveManager.delete(saveName);
            }
            saveListScreen.showSaves(saveManager.listSaves());
        });

        saveListScreen.setOnBack(() -> {
            refreshMenu();
            showScreen(menuScreen);
        });

        gameScreen.setOnBackToMenu(() -> {
            refreshMenu();
            showScreen(menuScreen);
        });
    }

    /** "Carica Partita" nel menu e' disabilitato se non esiste nessun salvataggio. */
    private void refreshMenu() {
        menuScreen.setLoadGameEnabled(saveManager.hasAnySave());
    }

    /**
     * Cambia la schermata mostrata nella finestra, senza aprire una nuova
     * finestra o ricreare la Scene.
     *
     * @param screen la nuova schermata da mostrare (MenuScreen, SaveListScreen o GameScreen)
     */
    private void showScreen(Region screen) {
        stage.getScene().setRoot(screen);
    }

    public static void main(String[] args) {
        launch(args);
    }
}