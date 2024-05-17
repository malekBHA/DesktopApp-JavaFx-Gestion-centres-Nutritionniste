package com.example.vitalize.Controlleur;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import com.example.vitalize.Entity.FichePatient;
import com.example.vitalize.Entity.Users;
import com.example.vitalize.Service.EmailService;
import com.example.vitalize.Service.FichePatientService;
import com.example.vitalize.Service.HealthAdvisorService;

import javax.mail.MessagingException;
import java.util.List;

public class AjouterFiche {

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

    @FXML
    void initialize() {
        loadUserNames();
    }

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

    @FXML
    void AddFiche(ActionEvent event) {
        if (isValidInput()) {
            try {
                Users selectedUser = comboPatient.getValue();
                if (selectedUser != null) {
                    FichePatient newFichePatient = new FichePatient(
                            selectedUser.getId(), // Assuming getId method is present in the Users model
                            Integer.parseInt(weightTF.getText()),
                            Integer.parseInt(muscleTF.getText()),
                            Integer.parseInt(heightTF.getText()),
                            allergiesTF.getText(),
                            illnessesTF.getText(),
                            breakfastTF.getText(),
                            middayTF.getText(),
                            dinnerTF.getText(),
                            snacksTF.getText(),
                            Integer.parseInt(caloriesTF.getText()),
                            otherTA.getText()
                    );

                    // Send email with patient's data and health advice
                    sendEmail(selectedUser.getEmail(), newFichePatient);

                    // Add the new fiche to the database
                    fps.add(newFichePatient);

                } else {
                    showErrorAlert("Please select a valid user.");
                }
            } catch (NumberFormatException e) {
                showErrorAlert("Please enter valid numbers for weight, muscle, height, and calories.");
            } catch (MessagingException e) {
                showErrorAlert("Error while sending the email: " + e.getMessage());
            }
        } else {
            showErrorAlert("Please enter valid data for all fields.");
        }
    }

    private void sendEmail(String recipient, FichePatient fichePatient) throws MessagingException {
        String subject = "New Health Report";
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

    private boolean isValidInput() {
        return isValidTextField(weightTF) &&
                isValidTextField(muscleTF) &&
                isValidTextField(heightTF) &&
                isValidTextField(caloriesTF) &&
                isValidStringField(allergiesTF) &&
                isValidStringField(illnessesTF) &&
                isValidStringField(breakfastTF) &&
                isValidStringField(middayTF) &&
                isValidStringField(dinnerTF) &&
                isValidCalories() &&
                isValidWeight() &&
                isValidHeight() &&
                isValidCalories() &&
                isValidMuscleMass() &&
                isValidStringField(snacksTF);
    }

    private boolean isValidTextField(TextField textField) {
        return textField.getText() != null && !textField.getText().isEmpty();
    }

    private boolean isValidCalories() {
        if (!isValidTextField(caloriesTF)) {
            return false;
        } else {
            String calories = caloriesTF.getText().trim();
            if (!calories.matches("\\d{1,4}")) {
                showErrorAlert("Calories should be a 4-digit number.");
                return false;
            }
        }
        return true;
    }

    private boolean isValidHeight() {
        if (!isValidTextField(heightTF)) {
            return false;
        } else {
            String height = heightTF.getText().trim();
            if (!height.matches("\\d{1,3}")) {
                showErrorAlert("Height should be a 3-digit number.");
                return false;
            }
        }
        return true;
    }

    private boolean isValidMuscleMass() {
        String muscleMassText = muscleTF.getText();
        if (!muscleMassText.matches("\\d{1,3}")) {
            showErrorAlert("Muscle mass should be a positive integer with maximum 3 digits.");
            return false;
        }
        return true;
    }

    private boolean isValidWeight() {
        if (!isValidTextField(weightTF)) {
            return false;
        } else {
            String weight = weightTF.getText().trim();
            if (!weight.matches("\\d{1,3}")) {
                showErrorAlert("Weight should be a 3-digit number.");
                return false;
            }
        }
        return true;
    }

    private boolean isValidStringField(TextField textField) {
        return isValidTextField(textField) && textField.getText().matches("[a-zA-Z\\s-]+");
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Input Error");
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void loadUserNames(ActionEvent event) {
        List<Users> userList = fps.getAllUserNames();
        comboPatient.getItems().addAll(userList);
    }
    @FXML
    void naviguer(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/vitalize/AfficherFiche.fxml"));
            Scene scene = new Scene(root);
            Stage window = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            window.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
