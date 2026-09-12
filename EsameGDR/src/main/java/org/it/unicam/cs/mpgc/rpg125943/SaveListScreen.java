package org.it.unicam.cs.mpgc.rpg125943;

import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Schermata con l'elenco dei salvataggi disponibili. Espone solo le azioni
 * (carica uno, elimina uno o piu', torna indietro) tramite callback: non sa
 * nulla di {@code SaveManager} o di come i salvataggi vengono letti/scritti.
 */
public class SaveListScreen extends VBox {

    private final ListView<String> savesListView = new ListView<>();
    private final Button loadButton = new Button("Carica Salvataggio");
    private final Button deleteButton = new Button("Elimina Salvataggio");
    private final Button backButton = new Button("Torna al menu");

    private Consumer<String> onLoad;
    private Consumer<List<String>> onDelete;

    public SaveListScreen() {
        savesListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);//selezione multipla

        loadButton.setDisable(true);
        deleteButton.setDisable(true);

        savesListView.getSelectionModel().getSelectedItems().addListener((ListChangeListener<String>) change -> {
            int selectedCount = savesListView.getSelectionModel().getSelectedItems().size();
            loadButton.setDisable(selectedCount != 1);
            deleteButton.setDisable(selectedCount == 0);
        });

        loadButton.setOnAction(event -> {
           List<String> selected = new ArrayList<>(savesListView.getSelectionModel().getSelectedItems());
           if (selected.size() == 1 && onLoad != null) {
               onLoad.accept(selected.get(0));
           }
        });

        deleteButton.setOnAction(event -> {
           List<String> selected = new ArrayList<>(savesListView.getSelectionModel().getSelectedItems());
           if(selected.isEmpty()){
               return;
           }

           Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                   "Eliminare" + selected.size() + " il salvataggio selezionato?",
                   ButtonType.YES, ButtonType.NO);
           confirm.setHeaderText(null);
           confirm.setTitle("Conferma eliminazione");

           Optional<ButtonType> answer = confirm.showAndWait();
           if(answer.isPresent() && answer.get() == ButtonType.YES && onDelete != null) {
               onDelete.accept(selected);
           }
        });

        HBox controls = new HBox(10, loadButton, deleteButton, backButton);
        getChildren().addAll(new Label("Salvataggi disponibili"), savesListView, controls);
        setSpacing(15);
        setPadding(new Insets(20));
    }

    public void setOnLoad(Consumer<String> onLoad) {
        this.onLoad = onLoad;
    }

    public void setOnDelete(Consumer<List<String>> onDelete) {
        this.onDelete = onDelete;
    }

    public void setOnBack(Runnable action) {
        backButton.setOnAction(event -> action.run());
    }

    public void showSaves(List<String> saveNames) {
        savesListView.getItems().setAll(saveNames);
    }
}
