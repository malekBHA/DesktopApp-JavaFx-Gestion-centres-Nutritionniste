package com.example.vitalize.Controlleur;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import com.example.vitalize.Entity.Reclamation;
import com.example.vitalize.Entity.Reponse;
import com.example.vitalize.Entity.UserSession;
import com.example.vitalize.Service.ReclamationService;
import com.example.vitalize.Service.ReponseService;

import java.util.List;
import java.util.stream.Collectors;

public class ReponsePatient {

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







        // Add columns to the table
        reponsesTable.getColumns().addAll( sujetCol, responseCol);

        // Load responses into the table
        loadResponses();
    }

    private void loadResponses() {
        int userId = UserSession.getInstance().getUser().getId(); // Cast Long to int

        Task<List<Reponse>> task = new Task<>() {
            @Override
            protected List<Reponse> call() throws Exception {
                // Fetch all responses
                List<Reponse> allResponses = reponseService.getAll();
                // Filter responses where the reclamation's user_id matches the session user ID
                return allResponses.stream()
                        .filter(reponse -> {
                            Reclamation reclamation = reclamationService.getOne(reponse.getReclamation_id());
                            return reclamation != null && reclamation.getUser_id() == userId; // Use == since userId is now int
                        })
                        .collect(Collectors.toList());
            }
        };

        task.setOnSucceeded(event -> {
            List<Reponse> filteredResponses = task.getValue();
            reponsesTable.getItems().clear(); // Clear existing items
            reponsesTable.getItems().addAll(filteredResponses); // Add new items
        });

        task.setOnFailed(event -> {
            Throwable exception = task.getException();
            if (exception != null) {
                exception.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Load Failed");
                alert.setContentText("Failed to load responses: " + exception.getMessage());
                alert.showAndWait();
            }
        });

        new Thread(task).start();
    }



}
