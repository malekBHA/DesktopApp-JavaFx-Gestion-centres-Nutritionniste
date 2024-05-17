package com.example.vitalize.Controlleur;

import com.itextpdf.text.*;
import com.itextpdf.text.Font;
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
import com.example.vitalize.Service.ReclamationService;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ReclamationBOffice {

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

    private final ReclamationService rs = new ReclamationService();

    @FXML
    void initialize() {
        try {
            List<Reclamation> reclamations = rs.getAll();
            ObservableList<Reclamation> observableList = FXCollections.observableList(reclamations);
            tableId.setItems(observableList);

            typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
            sujetCol.setCellValueFactory(new PropertyValueFactory<>("sujet"));
            dateCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDate().toString()));

            setupActionsColumn();
            setupPdfColumn();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void setupActionsColumn() {
        actionsCol.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Delete");
            private final Button updateButton = new Button("Update");
            private final HBox buttonsBox = new HBox(deleteButton, updateButton);

            {
                deleteButton.setOnAction(event -> {
                    try {
                        handleDeleteAction();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });
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

            private void handleDeleteAction() throws SQLException {
                Reclamation reclamation = tableId.getSelectionModel().getSelectedItem();
                if (reclamation != null) {
                    rs.delete(reclamation);
                    tableId.getItems().remove(reclamation); // Remove from TableView
                } else {
                    // No reclamation selected
                    showErrorMessage("Delete Error", "This reclamation has related responses and cannot be deleted.");
                }
            }

            private void showErrorMessage(String title, String message) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(title);
                alert.setHeaderText(null);
                alert.setContentText(message);
                alert.showAndWait();
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

                    Scene scene = new Scene(root);
                    Stage stage = new Stage();
                    stage.setScene(scene);
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
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
                        throw new RuntimeException(e);
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

    public void generatePDF(Reclamation reclamation) throws DocumentException, IOException {
        System.out.println("Generating PDF for Reclamation: " + reclamation.getId());

        Document document = new Document();
        try {
            String filename = "ReclamationDetails.pdf";
            PdfWriter.getInstance(document, new FileOutputStream(filename));

            document.open();

            // Set metadata
            document.addTitle("Reclamation Details");
            document.addAuthor("Your Name");
            document.addSubject("Reclamation Information");

            // Define fonts
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLACK);
            Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK);

            // Add title
            Paragraph title = new Paragraph("Reclamation Details", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            // Add reclamation details
            document.add(new Paragraph("\n")); // Add blank line for spacing
            document.add(new Paragraph("Type: " + reclamation.getType(), normalFont));
            document.add(new Paragraph("Date: " + reclamation.getDate(), normalFont));
            document.add(new Paragraph("Sujet: " + reclamation.getSujet(), normalFont));
            document.add(new Paragraph("Description: " + reclamation.getDescription(), normalFont));

            // Add image if imagePath is not null
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

    public void navigateToAddReclamation(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/vitalize/AjoutReclamation.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Add Reclamation");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void NavToAcceuil(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/vitalize/Admin.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void NavToStatistics(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/vitalize/Statistics.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void NavToReponses(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/vitalize/ReponsesBOffice.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
