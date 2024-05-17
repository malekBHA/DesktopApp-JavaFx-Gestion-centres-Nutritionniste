package com.example.vitalize.Controlleur;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import com.example.vitalize.Entity.RendezVous;
import com.example.vitalize.Service.RendezVousService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

public class EditerRdv {

    @FXML
    private Button AppList;

    @FXML
    private Spinner<Integer> HoursSpinner;

    @FXML
    private DatePicker datePicker;

    @FXML
    private RadioButton inPersonR;

    @FXML
    private RadioButton onlineR;

    private RendezVous selectedRdv;
    private final RendezVousService rendezVousService = new RendezVousService();
    public void initData(RendezVous rdv) {
        selectedRdv = rdv;

        datePicker.setValue(selectedRdv.getDate().toLocalDate());
        HoursSpinner.getValueFactory().setValue(selectedRdv.getDate().getHour()); // Set selected hour

    }

    @FXML
    void UpdateRdv(ActionEvent event) {
        try {
            if (selectedRdv != null) {
                LocalDateTime now = LocalDateTime.now();

                LocalDate selectedDate = datePicker.getValue();
                int selectedHour = HoursSpinner.getValue();

                String selectedType = inPersonR.isSelected() ? inPersonR.getText() : onlineR.getText();

                if (selectedDate == null) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Missing Date", "Please select a date for the appointment.");
                    return;
                }
                if (inPersonR.isSelected() && onlineR.isSelected()) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Multiple Types Selected", "Please select only one type of appointment (In person or Online).");
                    return;
                }
                if (selectedDate.isBefore(now.toLocalDate())) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Invalid Date", "Please select a date starting from today.");
                    return;
                }

                if (selectedDate.isEqual(now.toLocalDate()) && selectedHour < now.getHour()) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Invalid Time", "Please select a time starting from the current hour.");
                    return;
                }

                // Update the selected rendezvous with the new date, time, and type
                LocalDateTime selectedDateTime = LocalDateTime.of(selectedDate, LocalTime.of(selectedHour, 0));
                selectedRdv.setDate(selectedDateTime);
                selectedRdv.setType(selectedType);

                // Check for conflicts
                if (rendezVousService.exists(selectedRdv)) {
                    showAlert(Alert.AlertType.ERROR, "Error", "This Date and Time Already Booked", "An appointment for the selected date and time already exists. Please choose another date or time.");
                    return;
                }

                // If everything is fine, update the appointment
                rendezVousService.update(selectedRdv);
                showAlert(Alert.AlertType.INFORMATION, "Success", "RendezVous Updated", "RendezVous updated successfully.");
            } else {
                createNewAppointment();
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error updating RendezVous", e.getMessage());
        }
    }




    private void createNewAppointment() {
        try {
            LocalDate selectedDate = datePicker.getValue();
            int selectedHour = HoursSpinner.getValue();
            LocalDateTime selectedDateTime = LocalDateTime.of(selectedDate, LocalTime.of(selectedHour, 0));

            RendezVous newRdv = new RendezVous();
            newRdv.setDate(selectedDateTime);

            rendezVousService.add(newRdv);

            showAlert(Alert.AlertType.INFORMATION, "Success", "Appointment Created", "Appointment created successfully.");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error creating appointment", e.getMessage());
        }
    }

    @FXML
    void NaviguerRdv(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/vitalize/AfficherRdv.fxml"));
            Scene scene = new Scene(root);
            Stage window = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            window.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    public void initialize() {
        SpinnerValueFactory.IntegerSpinnerValueFactory valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(8, 18, 8);
        HoursSpinner.setValueFactory(valueFactory);
    }

    @FXML
    void DeleteR(ActionEvent event) {
        if (selectedRdv == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "No RendezVous selected", "Please select a RendezVous to delete.");
            return;
        }

        Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationDialog.setTitle("Confirmation");
        confirmationDialog.setHeaderText("Delete Appointment");
        confirmationDialog.setContentText("Are you sure you want to delete this appointment?");

        ButtonType buttonTypeOK = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        confirmationDialog.getButtonTypes().setAll(buttonTypeOK, buttonTypeCancel);

        Optional<ButtonType> result = confirmationDialog.showAndWait();

        if (((Optional<?>) result).isPresent() && result.get() == buttonTypeOK) {
            try {
                rendezVousService.delete(selectedRdv);

                showAlert(Alert.AlertType.INFORMATION, "Success", "Appointment Deleted", "Appointment deleted successfully.");
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Error deleting RendezVous", e.getMessage());
            }
        }
    }


}
