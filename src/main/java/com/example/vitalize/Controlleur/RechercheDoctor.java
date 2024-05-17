package com.example.vitalize.Controlleur;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import com.example.vitalize.Entity.UserSession;
import com.example.vitalize.Entity.Users;
import com.example.vitalize.Service.RendezVousService;

import java.io.IOException;
import java.util.List;

public class RechercheDoctor {

    private RendezVousService rendezVousService;

    public RechercheDoctor() {
        rendezVousService = new RendezVousService();
    }

    @FXML
    private TableColumn<Users, Integer> idColumn;

    @FXML
    private TableColumn<Users, String> nomColumn;

    @FXML
    private TableColumn<Users, String> prenomColumn;

    @FXML
    private TableColumn<Users, String> phoneColumn;

    @FXML
    private TableColumn<Users, String> emailColumn;

    @FXML
    private TableView<Users> searchtabdoc;

    @FXML
    private TextField searchdoc;

    @FXML
    private void initialize() {
        // Initialize table columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenomColumn.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("tel"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        // Add listener for row selection
        searchtabdoc.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                Users selectedDoctor = newSelection;
                openRendezVous(selectedDoctor.getId()); // Pass the ID of the selected doctor
            }
        });
    }

    private void openRendezVous(int selectedDoctorId) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/vitalize/RendezVous.fxml"));
            Parent root = loader.load();

            // Pass the selected doctor ID to the RendezVous controller
            RendezVous rendezVousController = loader.getController();
            rendezVousController.setDoctorId(selectedDoctorId);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @FXML
    private void search() {
        String searchText = searchdoc.getText().trim();
        if (!searchText.isEmpty()) {
            List<Users> usersList = rendezVousService.searchUsersByName(searchText);
            // Update table with search results
            ObservableList<Users> observableUsersList = FXCollections.observableArrayList(usersList);
            searchtabdoc.setItems(observableUsersList);
        }
    }

    @FXML
    private void logout() {
        UserSession.getInstance().logout();
        // Redirect to the login page
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/vitalize/Login.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
            // Close the current window
            Stage currentStage = (Stage) searchtabdoc.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
