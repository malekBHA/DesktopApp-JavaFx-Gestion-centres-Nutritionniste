package com.example.vitalize.Controlleur;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import com.example.vitalize.Entity.RendezVous;
import com.example.vitalize.Service.RendezVousService;

import java.time.LocalDateTime;
import java.util.List;

public class AfficherRdv {
    @FXML
    private TableView<RendezVous> tableViewRdv;

    @FXML
    private TableColumn<RendezVous, Integer> rdvIdCol;

    @FXML
    private TableColumn<RendezVous, Integer> doctorIdCol;

    @FXML
    private TableColumn<RendezVous, String> typeCol;

    @FXML
    private TableColumn<RendezVous, Boolean> availableCol;

    @FXML
    private TableColumn<RendezVous, LocalDateTime> dateCol;

    private RendezVous selectedRdv;

    private final RendezVousService rendezVousService = new RendezVousService();

    @FXML
    void initialize() {
        try {
            // Fetch data from the database
            List<RendezVous> rendezVousList = rendezVousService.getAll();

            // Create an observable list
            ObservableList<RendezVous> observableList = FXCollections.observableList(rendezVousList);

            // Populate the TableView
            tableViewRdv.setItems(observableList);

            // Configure TableView columns
            rdvIdCol.setCellValueFactory(new PropertyValueFactory<>("rdvId"));
            doctorIdCol.setCellValueFactory(new PropertyValueFactory<>("doctorId"));
            dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
            typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
            availableCol.setCellValueFactory(new PropertyValueFactory<>("available"));


            tableViewRdv.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1) {
                    RendezVous selectedRdv = tableViewRdv.getSelectionModel().getSelectedItem();
                    if (selectedRdv != null) {
                        try {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/vitalize/EditerRdv.fxml"));
                            Parent root = loader.load();
                            EditerRdv controller = loader.getController();
                            controller.initData(selectedRdv);
                            Scene scene = new Scene(root);
                            Stage window = (Stage) tableViewRdv.getScene().getWindow();
                            window.setScene(scene);
                        } catch (Exception e) {
                            showAlert(Alert.AlertType.ERROR, "Error", "Error loading EditerRdv", e.getMessage());
                        }
                    }
                }
            });
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error loading data", e.getMessage());
        }
    }


    @FXML
    void NaviguerRdv(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/vitalize/RechercheDoctor.fxml"));
            Scene scene = new Scene(root);
            Stage window = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            window.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    void NavigateToForms(ActionEvent event1) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/vitalize/AfficherFiche.fxml"));
            Scene scene = new Scene(root);
            Stage window = (Stage) ((javafx.scene.Node) event1.getSource()).getScene().getWindow();
            window.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
