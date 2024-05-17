package com.example.vitalize.Controlleur;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import com.example.vitalize.Entity.Reclamation;
import com.example.vitalize.Entity.Reponse;
import com.example.vitalize.Service.ReclamationService;
import com.example.vitalize.Service.ReponseService;

import java.io.IOException;
import java.util.List;

public class ReponsesBOffice {

    @FXML
    private TableView<Reponse> reponsesTable;

    private final ReclamationService reclamationService = new ReclamationService();
    private final ReponseService reponseService = new ReponseService();

    @FXML
    public void initialize() {
        // Define table columns
        TableColumn<Reponse, Integer> reclamationIdCol = new TableColumn<>("Reclamation ID");
        reclamationIdCol.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getReclamation_id()).asObject());

        TableColumn<Reponse, String> sujetCol = new TableColumn<>("Sujet");
        sujetCol.setCellValueFactory(cellData -> {
            int reclamationId = cellData.getValue().getReclamation_id();
            Reclamation reclamation = reclamationService.getOne(reclamationId);
            return new SimpleStringProperty(reclamation.getSujet());
        });

        TableColumn<Reponse, String> responseCol = new TableColumn<>("Response");
        responseCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMessage()));

        TableColumn<Reponse, Void> actionsCol = new TableColumn<>("Actions");
        actionsCol.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Delete");
            private final Button editButton = new Button("Edit");

            {
                deleteButton.setOnAction(event -> {
                    Reponse reponse = getTableView().getItems().get(getIndex());
                    handleDeleteReponse(reponse);
                });

                editButton.setOnAction(event -> {
                    Reponse reponse = getTableView().getItems().get(getIndex());
                    handleEditReponse(reponse);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    // Display delete and edit buttons
                    setGraphic(new VBox(deleteButton, editButton));
                    setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                }
            }
        });

        // Add columns to the table
        reponsesTable.getColumns().addAll(reclamationIdCol, sujetCol, responseCol, actionsCol);

        // Load responses into the table
        loadResponses();
    }

    private void handleDeleteReponse(Reponse reponse) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Delete Reponse");
        alert.setContentText("Are you sure you want to delete this reponse?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                reponseService.delete(reponse);
                loadResponses(); // Reload table after deletion
            }
        });
    }

    private void handleEditReponse(Reponse reponse) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/vitalize/EditReponse.fxml"));
            Parent root = loader.load();

            EditReponse editReponseController = loader.getController();
            editReponseController.initData(reponse);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Edit Reponse");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadResponses() {
        Task<List<Reponse>> task = new Task<>() {
            @Override
            protected List<Reponse> call() throws Exception {
                return reponseService.getAll();
            }
        };

        task.setOnSucceeded(event -> {
            List<Reponse> responses = task.getValue();
            reponsesTable.getItems().clear(); // Clear existing items
            reponsesTable.getItems().addAll(responses); // Add new items
        });


        task.setOnFailed(event -> {
            Throwable exception = task.getException();
            if (exception != null) {
                exception.printStackTrace();
            }
        });

        new Thread(task).start();
    }


public void NavToRecBO(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/vitalize/ReclamationBOffice.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


