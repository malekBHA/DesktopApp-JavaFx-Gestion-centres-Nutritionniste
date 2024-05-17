package com.example.vitalize.Controlleur;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import com.example.vitalize.Entity.UserSession;
import com.example.vitalize.Service.EmailService;
import com.example.vitalize.Service.RendezVousService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class AdminRendezVous {

    @FXML
    private RadioButton inPersonR;

    @FXML
    private RadioButton onlineR;

    @FXML
    private DatePicker datePicker;

    @FXML
    private TextField doctoridfixed;

    private int doctorId; // Store the doctorId

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId; // Set the doctorId
        doctoridfixed.setText(String.valueOf(doctorId));
        doctoridfixed.setDisable(true); // Disable the text field
    }

    private ToggleGroup typeGroup;

    @FXML
    private Spinner<Integer> HoursSpinner;

    @FXML
    public void initialize() {
        typeGroup = new ToggleGroup();
        inPersonR.setToggleGroup(typeGroup);
        onlineR.setToggleGroup(typeGroup);
        inPersonR.setSelected(true);

        // Configure the spinner to have values from 8 to 18
        SpinnerValueFactory.IntegerSpinnerValueFactory valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(8, 18, 8);
        HoursSpinner.setValueFactory(valueFactory);
    }

    @FXML
    void AddRdv(ActionEvent event) {
        // Get the current date and time
        LocalDateTime now = LocalDateTime.now();

        LocalDate selectedDate = datePicker.getValue();
        if (selectedDate == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Missing Date", "Please select a date for the appointment.");
            return;
        }

        if (selectedDate.isBefore(now.toLocalDate())) {
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid Date", "Please select a date starting from today.");
            return;
        }

        int selectedHour = HoursSpinner.getValue();
        if (selectedHour == 0) {
            showAlert(Alert.AlertType.ERROR, "Error", "Missing Time", "Please select a time for the appointment.");
            return;
        }

        if (selectedDate.isEqual(now.toLocalDate()) && selectedHour < now.getHour()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid Time", "Please select a time starting from the current hour.");
            return;
        }

        String selectedType = "";
        if (inPersonR.isSelected()) {
            selectedType = "In person";
        } else if (onlineR.isSelected()) {
            selectedType = "Online";
        }

        // Check if the selected type is valid
        if (!selectedType.equals("In person") && !selectedType.equals("Online")) {
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid Appointment Type", "Please select a valid appointment type (In person or Online).");
            return;
        }

        LocalDateTime selectedDateTime = LocalDateTime.of(selectedDate, LocalTime.of(selectedHour, 0));
        com.example.vitalize.Entity.RendezVous rdv = new com.example.vitalize.Entity.RendezVous();
        rdv.setDoctorId(Integer.parseInt(doctoridfixed.getText())); // Fetch the doctorId from the text field
        rdv.setType(selectedType);
        rdv.setDate(selectedDateTime);

        RendezVousService service = new RendezVousService();

        // Fetch the email and name of the current user
        UserSession userSession = UserSession.getInstance();
        String userEmail = userSession.getUser().getEmail();
        String userName = userSession.getUser().getNom(); // Fetch the name of the current user

        try {
            // Check if an appointment already exists for the selected date, time, and doctor
            boolean conflictingAppointment = service.exists(rdv);
            if (conflictingAppointment) {
                // Check if an appointment exists for the selected date, time, and doctor with a different type
                boolean conflictingType = service.existsForDifferentType(rdv);
                if (conflictingType) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Conflicting Appointment", "An appointment for the selected date and doctor already exists with a different type. Please choose another date or time.");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Conflicting Appointment", "An appointment for the selected date and doctor already exists. Please choose another date or time.");
                }
            } else {
                // Check if there is an appointment for the same doctor, date, and hour but with a different type
                boolean conflictingType = service.existsForDifferentType(rdv);
                if (conflictingType) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Conflicting Appointment", "An appointment for the selected doctor and hour already exists with a different type. Please choose another date or time.");
                } else {
                    service.add(rdv);
                    showAlert(Alert.AlertType.INFORMATION, "Success", "RendezVous Added", "New RendezVous added successfully.");

                    // Send confirmation email to the current user
                    String subject = "Appointment Confirmation";
                    String content = "Dear " + userName + ",\n\n";
                    content += "Your appointment has been scheduled successfully" + " at " + selectedDateTime.toString() + ".";

                    EmailService emailService = new EmailService();
                    emailService.sendEmail(userEmail, subject, content);

                    // Send confirmation email to the doctor
                    String doctorEmail = service.getDoctorEmail(Integer.parseInt(doctoridfixed.getText()));
                    String doctorName = service.getDoctorName(Integer.parseInt(doctoridfixed.getText()));

                    String doctorSubject = "New Appointment";
                    String doctorContent = "Dear Dr. " + doctorName + ",\n\n";
                    doctorContent += "You have a new appointment with " + userName + " at " + selectedDateTime.toString() + ".";
                    emailService.sendEmail(doctorEmail, doctorSubject, doctorContent);

                    // Send Google Meet link if the appointment is online
                    if ("Online".equals(selectedType)) {
                        String meetLink = generateMeetLink();
                        emailService.sendEmail(userEmail, "Google Meet Link", "Here is the Google Meet link for your appointment: " + meetLink);
                        emailService.sendEmail(doctorEmail, "Google Meet Link", "Here is the Google Meet link for the appointment with " + userName + ": " + meetLink);
                    }
                }
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Unexpected Error", e.getMessage());
        }
    }

    private String generateMeetLink() {
        // Generate and return the Google Meet link
        return "https://meet.google.com/ysc-fbvm-jft";
    }

    private void showAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    public void NaviguerRdv(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/vitalize/AdminAfficherRdv.fxml"));
            Scene scene = new Scene(root);
            Stage window = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            window.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
