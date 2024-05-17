package com.example.vitalize.Controlleur;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import com.example.vitalize.Entity.FichePatient;
import com.example.vitalize.Entity.Users;
import com.example.vitalize.Service.EmailService;
import com.example.vitalize.Service.FichePatientService;
import com.example.vitalize.Service.HealthAdvisorService;

import javax.mail.MessagingException;
import java.util.List;

public class AdminEditerFiche {

    @FXML
    private TextField allergiesTF;

    @FXML
    private TextField breakfastTF;

    @FXML
    private TextField caloriesTF;

    @FXML
    private TextField dinnerTF;

    @FXML
    private TextField heightTF;

    @FXML
    private TextField illnessesTF;

    @FXML
    private TextField middayTF;

    @FXML
    private TextField muscleTF;

    @FXML
    private TextArea otherTA;

    @FXML
    private TextField snacksTF;

    @FXML
    private TextField weightTF;

    @FXML
    private ComboBox<Users> comboPatient;

    private final FichePatientService fps = new FichePatientService();
    private final EmailService emailService = new EmailService();
    private final HealthAdvisorService healthAdvisorService = new HealthAdvisorService();

    private FichePatient selectedFichePatient;

    public void initData(FichePatient fichePatient) {
        this.selectedFichePatient = fichePatient;
        displayFichePatientData();
    }

    @FXML
    void initialize() {
        loadUserNames();
    }

    @FXML
    private void loadUserNames() {
        List<Users> userList = fps.getAllUserNames();
        comboPatient.getItems().addAll(userList);
        comboPatient.setCellFactory(param -> new ListCell<Users>() {
            @Override
            protected void updateItem(Users user, boolean empty) {
                super.updateItem(user, empty);
                if (empty || user == null) {
                    setText(null);
                } else {
                    setText(user.getNom()); // Assuming nom is the property you want to display
                }
            }
        });
        comboPatient.setConverter(new StringConverter<Users>() {
            @Override
            public String toString(Users user) {
                return user == null ? null : user.getNom(); // Assuming nom is the property you want to display
            }

            @Override
            public Users fromString(String string) {
                return null;
            }
        });
    }

    private void displayFichePatientData() {
        if (selectedFichePatient != null) {
            weightTF.setText(String.valueOf(selectedFichePatient.getWeight()));
            muscleTF.setText(String.valueOf(selectedFichePatient.getMuscleMass()));
            heightTF.setText(String.valueOf(selectedFichePatient.getHeight()));
            allergiesTF.setText(selectedFichePatient.getAllergies());
            illnessesTF.setText(selectedFichePatient.getIllnesses());
            breakfastTF.setText(selectedFichePatient.getBreakfast());
            middayTF.setText(selectedFichePatient.getMidday());
            dinnerTF.setText(selectedFichePatient.getDinner());
            snacksTF.setText(selectedFichePatient.getSnacks());
            caloriesTF.setText(String.valueOf(selectedFichePatient.getCalories()));
            otherTA.setText(selectedFichePatient.getOther());
            comboPatient.setValue(comboPatient.getValue());
        }
    }

    @FXML
    void updateFiche(ActionEvent event) {
        try {
            Users selectedUser = comboPatient.getValue();
            if (selectedFichePatient != null) {

                selectedFichePatient.setWeight(Integer.parseInt(weightTF.getText()));
                selectedFichePatient.setMuscleMass(Integer.parseInt(muscleTF.getText()));
                selectedFichePatient.setHeight(Integer.parseInt(heightTF.getText()));
                selectedFichePatient.setAllergies(allergiesTF.getText());
                selectedFichePatient.setIllnesses(illnessesTF.getText());
                selectedFichePatient.setBreakfast(breakfastTF.getText());
                selectedFichePatient.setMidday(middayTF.getText());
                selectedFichePatient.setDinner(dinnerTF.getText());
                selectedFichePatient.setSnacks(snacksTF.getText());
                selectedFichePatient.setCalories(Integer.parseInt(caloriesTF.getText()));
                selectedFichePatient.setOther(otherTA.getText());

                fps.update(selectedFichePatient);

                // Send email with patient's data and health advice
                sendEmail(selectedUser.getEmail(), selectedFichePatient);                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Update Successful");
                alert.setContentText("FichePatient updated successfully.");
                alert.showAndWait();
            }
        } catch (NumberFormatException e) {
            showErrorAlert("Please enter valid numbers for weight, muscle, height, and calories.");
        } catch (MessagingException e) {
            showErrorAlert("Error while sending the email: " + e.getMessage());
        }
    }


    private void sendEmail(String recipient, FichePatient fichePatient) throws MessagingException {
        String subject = "Updated Health Report";
        String content = "Patient Name: " + comboPatient.getValue().getNom() + "\n" +
                "Weight: " + fichePatient.getWeight() + " kg\n" +
                "Muscle Mass: " + fichePatient.getMuscleMass() + " kg\n" +
                "Height: " + fichePatient.getHeight() + " cm\n" +
                "Allergies: " + fichePatient.getAllergies() + "\n" +
                "Illnesses: " + fichePatient.getIllnesses() + "\n" +
                "Breakfast: " + fichePatient.getBreakfast() + "\n" +
                "Midday: " + fichePatient.getMidday() + "\n" +
                "Dinner: " + fichePatient.getDinner() + "\n" +
                "Snacks: " + fichePatient.getSnacks() + "\n" +
                "Calories: " + fichePatient.getCalories() + " kcal\n" +
                "Other: " + fichePatient.getOther() + "\n\n" +
                healthAdvisorService.provideHealthAdvice(fichePatient);
        emailService.sendEmail(recipient, subject, content);
    }


    @FXML
    void deleteFiche(ActionEvent event) {
        try {
            if (selectedFichePatient != null) {
                Alert confirmationAlert = new Alert(AlertType.CONFIRMATION);
                confirmationAlert.setTitle("Confirm Deletion");
                confirmationAlert.setHeaderText("Are you sure you want to delete this fiche patient?");
                confirmationAlert.setContentText("This action cannot be undone.");

                confirmationAlert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        try {
                            fps.delete(selectedFichePatient);
                            Alert successAlert = new Alert(AlertType.INFORMATION);
                            successAlert.setTitle("Delete Successful");
                            successAlert.setContentText("Fiche patient deleted successfully.");
                            successAlert.showAndWait();
                        } catch (Exception e) {
                            showErrorAlert("An error occurred while deleting the fiche patient.");
                        }
                    }
                });
            }
        } catch (Exception e) {
            showErrorAlert("An error occurred while deleting the fiche patient.");
        }
    }

    @FXML
    void naviguerAdd(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/vitalize/AdminAjouterFiche.fxml"));
            Scene scene = new Scene(root);
            Stage window = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            window.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void naviguerList(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/vitalize/AdminAfficherFiche.fxml"));
            Scene scene = new Scene(root);
            Stage window = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            window.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Input Error");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
