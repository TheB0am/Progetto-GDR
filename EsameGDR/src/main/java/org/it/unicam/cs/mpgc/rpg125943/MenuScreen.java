package org.it.unicam.cs.mpgc.rpg125943;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * Schermata di menu principale: tre bottoni (Nuova Partita, Carica Partita,
 * Esci). Non sa nulla di COME queste azioni vengono gestite: si limita ad
 * esporre dei "hook" ({@link #setOnNewGame(Runnable)}, ecc.) che chi la usa
 * ({@code App}) collega alla logica vera. Una sola responsabilita': mostrare
 * il menu e le sue azioni.
 */
public class MenuScreen extends VBox {

    private final Button newGameButton = new Button("Nuova Partita");
    private final Button loadGameButton = new Button("Carica Partita");
    private final Button exitButton = new Button("Esci");

    public MenuScreen() {
        Label title = new Label("WBC - World Boxing Championship");

        getChildren().addAll(title, newGameButton, loadGameButton, exitButton);

        setSpacing(20);
        setAlignment(Pos.CENTER);
        setPadding(new Insets(40));
    }

    public void setOnNewGame(Runnable action){
        newGameButton.setOnAction(e -> action.run());
    }

    public void setOnLoadGame(Runnable action){
        loadGameButton.setOnAction(e -> action.run());
    }

    public void setOnExit(Runnable action){
        exitButton.setOnAction(e -> action.run());
    }

    public void setLoadGameEnabled(boolean enable){
        loadGameButton.setDisable(!enable);
    }
}
