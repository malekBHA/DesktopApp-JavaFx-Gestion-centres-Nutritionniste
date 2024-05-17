package com.example.vitalize.Controlleur;

import com.example.vitalize.Service.ReponseService;
import com.itextpdf.text.*;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import com.example.vitalize.Entity.Reclamation;
import com.example.vitalize.Entity.UserSession;
import com.example.vitalize.Service.ReclamationService;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AfficherReclamation {

    @FXML
    private TableView<Reclamation> tableId;

    @FXML
    private TableColumn<Reclamation, String> typeCol;

    @FXML
    private TableColumn<Reclamation, String> sujetCol;

    @FXML
    private TableColumn<Reclamation, String> dateCol;

    @FXML
    private TableColumn<Reclamation, Void> actionsCol;

    @FXML
    private TableColumn<Reclamation, Double> progressCol;

    private final ReclamationService rs = new ReclamationService();
    private final ReponseService rp = new ReponseService();

    private final UserSession userSession = UserSession.getInstance();

    @FXML
    void initialize() {
        long userId = userSession.getUser().getId();

        List<Reclamation> allReclamations = rs.getAll();
        List<Reclamation> userReclamations = allReclamations.stream()
                .filter(r -> r.getUser_id() == userId)
                .collect(Collectors.toList());

        ObservableList<Reclamation> observableList = FXCollections.observableArrayList(userReclamations);
        tableId.setItems(observableList);

        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        sujetCol.setCellValueFactory(new PropertyValueFactory<>("sujet"));
        dateCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDate().toString()));

        setupActionsColumn();
        setupPdfColumn();
        setupProgressColumn();
    }

    private void setupActionsColumn() {
        actionsCol.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Delete");
            private final Button updateButton = new Button("Update");
            private final HBox buttonsBox = new HBox(deleteButton, updateButton);

            {
                deleteButton.setOnAction(event -> handleDeleteAction());
                updateButton.setOnAction(event -> handleUpdateAction());
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(buttonsBox);
                }
            }


            private void handleDeleteAction() {
                Reclamation reclamation = getTableView().getItems().get(getIndex());
                if (reclamation != null) {
                    // Show confirmation dialog before deleting
                    Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
                    confirmAlert.setTitle("Confirm Deletion");
                    confirmAlert.setHeaderText("Are you sure you want to delete this reclamation?");
                    confirmAlert.setContentText("This action is irreversible and will delete all related data.");

                    Optional<ButtonType> result = confirmAlert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        // Check if there are related responses
                        if (rp.hasRelatedResponses(reclamation.getId())) {
                            // Show error alert because deletion is not allowed
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Deletion Error");
                            alert.setHeaderText("Deletion Not Allowed");
                            alert.setContentText("This reclamation cannot be deleted because it has related responses.");
                            alert.showAndWait();
                        } else {
                            try {
                                // Proceed with deletion if no related responses
                                rs.delete(reclamation);
                                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                                successAlert.setTitle("Deletion Successful");
                                successAlert.setHeaderText(null);
                                successAlert.setContentText("The reclamation has been successfully deleted.");
                                successAlert.showAndWait();
                            } catch (Exception e) {
                                // Handle other errors during deletion
                                Alert failureAlert = new Alert(Alert.AlertType.ERROR);
                                failureAlert.setTitle("Deletion Failed");
                                failureAlert.setHeaderText(null);
                                failureAlert.setContentText("Failed to delete the reclamation. Please try again.");
                                failureAlert.showAndWait();
                            }
                        }
                    }
                } else {
                    // Alert if no reclamation is selected
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("No Reclamation Selected");
                    alert.setHeaderText(null);
                    alert.setContentText("Please select a reclamation to delete.");
                    alert.showAndWait();
                }
            }





            private void handleUpdateAction() {
                Reclamation reclamation = getTableView().getItems().get(getIndex());
                navigateToEditScene(reclamation);
            }

            private void navigateToEditScene(Reclamation reclamation) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/vitalize/EditReclamation.fxml"));
                    Parent root = loader.load();

                    EditReclamation controller = loader.getController();
                    controller.initData(reclamation);

                    Stage stage = new Stage();
                    stage.setScene(new Scene(root));
                    stage.setTitle("Edit Reclamation");
                    stage.show();
                } catch (IOException e) {
                    showErrorDialog("Navigation Error", "Failed to navigate to edit reclamation scene.");
                }
            }
        });
    }

    private void setupPdfColumn() {
        TableColumn<Reclamation, Void> pdfCol = new TableColumn<>("PDF");
        pdfCol.setPrefWidth(100);

        pdfCol.setCellFactory(param -> new TableCell<>() {
            private final Button pdfButton = new Button("Generate PDF");

            {
                pdfButton.setOnAction(event -> {
                    Reclamation reclamation = getTableView().getItems().get(getIndex());
                    try {
                        generatePDF(reclamation);
                    } catch (IOException | DocumentException e) {
                        showErrorDialog("PDF Generation Error", "Failed to generate PDF for reclamation.");
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(pdfButton);
                }
            }
        });

        tableId.getColumns().add(pdfCol);
    }

    private void setupProgressColumn() {
        progressCol.setCellFactory(param -> new TableCell<>() {
            private final ProgressBar progressBar = new ProgressBar();

            @Override
            protected void updateItem(Double progressValue, boolean empty) {
                super.updateItem(progressValue, empty);
                if (empty || progressValue == null) {
                    setGraphic(null);
                } else {
                    progressBar.setProgress(progressValue);
                    setProgressBarColor(progressBar, getTableRow().getItem());
                    setGraphic(progressBar);
                }
            }
        });

        progressCol.setCellValueFactory(cellData -> {
            Reclamation reclamation = cellData.getValue();
            Double progress = calculateProgress(reclamation);
            return javafx.beans.binding.Bindings.createObjectBinding(() -> progress);
        });
    }



    private Double calculateProgress(Reclamation reclamation) {
        if (reclamation == null || reclamation.getEtat() == null) {
            return 0.0; // Default progress
        }

        switch (reclamation.getEtat()) {
            case "En Attente":
                return 0.25; // 25% progress
            case "En Cours":
                return 0.5;  // 50% progress
            case "Resolu":
                return 1.0;  // 100% progress
            default:
                return 0.0;  // Default progress
        }
    }

    private void setProgressBarColor(ProgressBar progressBar, Reclamation reclamation) {
        if (reclamation != null && reclamation.getEtat() != null) {
            String etat = reclamation.getEtat();
            switch (etat) {
                case "En Attente":
                    progressBar.setStyle("-fx-accent: yellow;");
                    break;
                case "En Cours":
                    progressBar.setStyle("-fx-accent: blue;");
                    break;
                case "Resolu":
                    progressBar.setStyle("-fx-accent: green;");
                    break;
                default:
                    progressBar.setStyle("-fx-accent: blue;");
                    break;
            }
        } else {
            progressBar.setStyle("-fx-accent: blue;");
        }
    }

    private void generatePDF(Reclamation reclamation) throws DocumentException, IOException {
        System.out.println("Generating PDF for Reclamation: " + reclamation.getId());

        Document document = new Document();
        try {
            String filename = "ReclamationDetails.pdf";
            PdfWriter.getInstance(document, new FileOutputStream(filename));

            document.open();

            // Add reclamation details to the PDF
            document.add(new Paragraph("Type: " + reclamation.getType()));
            document.add(new Paragraph("Date: " + reclamation.getDate()));
            document.add(new Paragraph("Sujet: " + reclamation.getSujet()));
            document.add(new Paragraph("Description: " + reclamation.getDescription()));

            // Add image if available
            if (reclamation.getFile() != null) {
                Image image = Image.getInstance(reclamation.getFile());
                image.scaleToFit(400, 300); // Adjust image size if needed
                document.add(image);
            }

            // Close the document
            document.close();
            System.out.println("PDF generated successfully.");

            // Open the generated PDF file using the default PDF viewer
            File pdfFile = new File(filename);
            if (pdfFile.exists()) {
                Desktop.getDesktop().open(pdfFile);
            } else {
                System.out.println("PDF file not found.");
            }
        } catch (FileNotFoundException | DocumentException e) {
            e.printStackTrace();
            throw new DocumentException("Error occurred during PDF generation: " + e.getMessage());
        }
    }

    private void showErrorDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void navigateToAddReclamation() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/vitalize/AjoutReclamation.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Add Reclamation");
            stage.show();
        } catch (IOException e) {
            showErrorDialog("Navigation Error", "Failed to navigate to add reclamation scene.");
        }
    }

    public void navigateToReponsePatient(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/vitalize/ReponsePatient.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showErrorDialog("Navigation Error", "Failed to navigate to add reponse Patient scene.");
        }
    }
}
