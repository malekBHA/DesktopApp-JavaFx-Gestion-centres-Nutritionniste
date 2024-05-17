package com.example.vitalize.Controlleur;

import com.example.vitalize.Controlleur.AjoutReponse;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import com.example.vitalize.Entity.Reclamation;
import com.example.vitalize.Service.ReclamationService;
import com.example.vitalize.Entity.UserSession;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class AfficherRecRep {

    @FXML
    private TableView<Reclamation> tableId;

    @FXML
    private TableColumn<Reclamation, String> typeCol;

    @FXML
    private TableColumn<Reclamation, String> sujetCol;

    @FXML
    private TableColumn<Reclamation, Date> dateCol;

    private final ReclamationService rs = new ReclamationService();

    private final UserSession userSession = UserSession.getInstance();

    @FXML
    void initialize() {
        int medecinId = userSession.getUser().getId(); // Assuming user.getId() returns medecin_id as an int

        // Get reclamations filtered by medecin_id
        List<Reclamation> reclamations = rs.getAll().stream()
                .filter(r -> r.getMedecin() == medecinId) // Compare int values using ==
                .collect(Collectors.toList());

        ObservableList<Reclamation> observableList = FXCollections.observableList(reclamations);
        tableId.setItems(observableList);
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        sujetCol.setCellValueFactory(new PropertyValueFactory<>("sujet"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));

        // Add actions column (Respond button)
        TableColumn<Reclamation, Void> actionsCol = new TableColumn<>("Actions");
        actionsCol.setPrefWidth(100);
        actionsCol.setCellFactory(param -> new TableCell<>() {
            private final Button respondButton = new Button("Repondre");

            {
                respondButton.setOnAction(event -> {
                    Reclamation reclamation = getTableView().getItems().get(getIndex());
                    navigateToReponseScene(reclamation);
                });
            }

            private void navigateToReponseScene(Reclamation reclamation) {
                try {
                    System.out.println("Navigating to response scene with reclamation ID: " + reclamation.getId()); // Log the ID being passed
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/vitalize/AjoutReponse.fxml"));
                    Parent root = loader.load();
                    AjoutReponse ajoutReponseController = loader.getController();
                    ajoutReponseController.setReclamationId(reclamation.getId()); // Set the ID in the target controller
                    Scene scene = new Scene(root);
                    Stage stage = new Stage();
                    stage.setScene(scene);
                    stage.show();
                    System.out.println("AjoutReponse.fxml loaded successfully.");
                } catch (IOException e) {
                    e.printStackTrace();
                    System.err.println("Failed to load AjoutReponse.fxml: " + e.getMessage());
                }
            }


            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(respondButton);
                }
            }
        });

        tableId.getColumns().add(actionsCol);
    }
}
