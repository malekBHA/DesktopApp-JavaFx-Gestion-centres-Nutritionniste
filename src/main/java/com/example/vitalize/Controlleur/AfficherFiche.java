package com.example.vitalize.Controlleur;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import com.example.vitalize.Entity.FichePatient;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.example.vitalize.Service.FichePatientService;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Objects;

public class AfficherFiche {
    @FXML
    private TableView<FichePatient> TableViewFiche;

    @FXML
    private TableColumn<FichePatient, String> allergiesCol;

    @FXML
    private TableColumn<FichePatient, String> breakfastCol;

    @FXML
    private TableColumn<FichePatient, Integer> caloriesCol;

    @FXML
    private TableColumn<FichePatient, String> dinnerCol;

    @FXML
    private TableColumn<FichePatient, Integer> heightCol;

    @FXML
    private TableColumn<FichePatient, String> illnessesCol;

    @FXML
    private TableColumn<FichePatient, String> middayCol;

    @FXML
    private TableColumn<FichePatient, Integer> musclCol;

    @FXML
    private TableColumn<FichePatient, String> otherCol;

    @FXML
    private TableColumn<FichePatient, String> snacksCol;

    @FXML
    private Button createnewformbtn;

    @FXML
    private TableColumn<FichePatient, Integer> weightCol;

    @FXML
    private TableColumn<FichePatient, Integer> ExcelCol;

    @FXML
    private TableColumn<FichePatient, Integer> PdfCol;

    private final FichePatientService fps = new FichePatientService();

    @FXML
    void initialize() {
        try {
            List<FichePatient> fichePatients = fps.Afficher();
            ObservableList<FichePatient> observableList = FXCollections.observableList(fichePatients);
            TableViewFiche.setItems(observableList);
            weightCol.setCellValueFactory(new PropertyValueFactory<>("weight"));
            musclCol.setCellValueFactory(new PropertyValueFactory<>("muscleMass"));
            heightCol.setCellValueFactory(new PropertyValueFactory<>("height"));
            allergiesCol.setCellValueFactory(new PropertyValueFactory<>("allergies"));
            illnessesCol.setCellValueFactory(new PropertyValueFactory<>("illnesses"));
            breakfastCol.setCellValueFactory(new PropertyValueFactory<>("breakfast"));
            middayCol.setCellValueFactory(new PropertyValueFactory<>("midday"));
            dinnerCol.setCellValueFactory(new PropertyValueFactory<>("dinner"));
            snacksCol.setCellValueFactory(new PropertyValueFactory<>("snacks"));
            caloriesCol.setCellValueFactory(new PropertyValueFactory<>("calories"));
            otherCol.setCellValueFactory(new PropertyValueFactory<>("other"));

            // Add a button to the ExcelCol column
            ExcelCol.setCellFactory(column -> {
                return new TableCell<FichePatient, Integer>() {
                    final Button excelButton = new Button("Excel");

                    {
                        excelButton.setOnAction(event -> {
                            FichePatient fichePatient = getTableView().getItems().get(getIndex());
                            generateExcel(fichePatient);
                        });
                    }

                    @Override
                    protected void updateItem(Integer item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(excelButton);
                        }
                    }
                };
            });

            // Add a button to the PdfCol column
            PdfCol.setCellFactory(column -> {
                return new TableCell<FichePatient, Integer>() {
                    final Button pdfButton = new Button("PDF");

                    {
                        pdfButton.setOnAction(event -> {
                            FichePatient fichePatient = getTableView().getItems().get(getIndex());
                            generatePdf(fichePatient);
                        });
                    }

                    @Override
                    protected void updateItem(Integer item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(pdfButton);
                        }
                    }
                };
            });

            // Handle row selection for editing
            TableViewFiche.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1) { // Detect double click
                    FichePatient selectedFichePatient = TableViewFiche.getSelectionModel().getSelectedItem();
                    if (selectedFichePatient != null) {
                        try {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/vitalize/EditerFiche.fxml"));
                            Parent root = loader.load();
                            EditerFiche controller = loader.getController();
                            controller.initData(selectedFichePatient);

                            Scene scene = new Scene(root);
                            Stage window = (Stage) TableViewFiche.getScene().getWindow();
                            window.setScene(scene);
                        } catch (Exception e) {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Input Error");
                            alert.setContentText(e.getMessage());
                            alert.showAndWait();
                        }
                    }
                }
            });

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Input Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }


    @FXML
    void updateSelectedFiche(ActionEvent event) {
        FichePatient selectedFichePatient = TableViewFiche.getSelectionModel().getSelectedItem();
        if (selectedFichePatient != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/vitalize/EditerFiche.fxml"));
                Parent root = loader.load();
                EditerFiche controller = loader.getController();
                controller.initData(selectedFichePatient);

                Scene scene = new Scene(root);
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window.setScene(scene);
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Input Error");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
        }
    }

    @FXML
    void CreateNewFormBtn(ActionEvent event2) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/example/vitalize/AjouterFiche.fxml")));
            Scene scene = new Scene(root);
            Stage window = (Stage) ((Node) event2.getSource()).getScene().getWindow();
            window.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    void NavToRdv(ActionEvent event4) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/vitalize/RechercheDoctor.fxml"));
            Scene scene = new Scene(root);
            Stage window = (Stage) ((Node) event4.getSource()).getScene().getWindow();
            window.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void generateExcel(FichePatient fichePatient) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("FichePatient");

        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("Weight");
        row.createCell(1).setCellValue("Muscle Mass");
        row.createCell(2).setCellValue("Height");
        row.createCell(3).setCellValue("Allergies");
        row.createCell(4).setCellValue("Illnesses");
        row.createCell(5).setCellValue("Breakfast");
        row.createCell(6).setCellValue("Midday");
        row.createCell(7).setCellValue("Dinner");
        row.createCell(8).setCellValue("Snacks");
        row.createCell(9).setCellValue("Calories");
        row.createCell(10).setCellValue("Other");

        row = sheet.createRow(1);
        row.createCell(0).setCellValue(fichePatient.getWeight());
        row.createCell(1).setCellValue(fichePatient.getMuscleMass());
        row.createCell(2).setCellValue(fichePatient.getHeight());
        row.createCell(3).setCellValue(fichePatient.getAllergies());
        row.createCell(4).setCellValue(fichePatient.getIllnesses());
        row.createCell(5).setCellValue(fichePatient.getBreakfast());
        row.createCell(6).setCellValue(fichePatient.getMidday());
        row.createCell(7).setCellValue(fichePatient.getDinner());
        row.createCell(8).setCellValue(fichePatient.getSnacks());
        row.createCell(9).setCellValue(fichePatient.getCalories());
        row.createCell(10).setCellValue(fichePatient.getOther());

        try {
            int fileNumber = 1;
            String fileName = "D:\\FichePatient" + fileNumber + ".xlsx";
            File file = new File(fileName);
            while (file.exists()) {
                fileNumber++;
                fileName = "D:\\FichePatient" + fileNumber + ".xlsx";
                file = new File(fileName);
            }

            FileOutputStream fileOut = new FileOutputStream(file);
            workbook.write(fileOut);
            fileOut.close();
            workbook.close();
            System.out.println("Excel file generated successfully: " + fileName);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Excel file generated successfully: " + fileName);
            alert.showAndWait();
        } catch (Exception e) {
            System.err.println("Error occurred while generating Excel file: " + e.getMessage());
        }
    }

    private void generatePdf(FichePatient fichePatient) {
        try {
            int fileNumber = 1;
            String fileName = "D:\\FichePatient" + fileNumber + ".pdf";
            File file = new File(fileName);
            while (file.exists()) {
                fileNumber++;
                fileName = "D:\\FichePatient" + fileNumber + ".pdf";
                file = new File(fileName);
            }

            PDDocument document = new PDDocument();
            PDPage page = new PDPage();
            document.addPage(page);

            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
            contentStream.beginText();
            contentStream.setLeading(14.5f); // Set line spacing
            contentStream.newLineAtOffset(50, 750);


            contentStream.showText("Weight: " + fichePatient.getWeight());
            contentStream.newLineAtOffset(0, -20);
            contentStream.showText("Muscle Mass: " + fichePatient.getMuscleMass());
            contentStream.newLineAtOffset(0, -20);
            contentStream.showText("Height: " + fichePatient.getHeight());
            contentStream.newLineAtOffset(0, -20);
            contentStream.showText("Allergies: " + fichePatient.getAllergies());
            contentStream.newLineAtOffset(0, -20);
            contentStream.showText("Illnesses: " + fichePatient.getIllnesses());
            contentStream.newLineAtOffset(0, -20);
            contentStream.showText("Breakfast: " + fichePatient.getBreakfast());
            contentStream.newLineAtOffset(0, -20);
            contentStream.showText("Midday: " + fichePatient.getMidday());
            contentStream.newLineAtOffset(0, -20);
            contentStream.showText("Dinner: " + fichePatient.getDinner());
            contentStream.newLineAtOffset(0, -20);
            contentStream.showText("Snacks: " + fichePatient.getSnacks());
            contentStream.newLineAtOffset(0, -20);
            contentStream.showText("Calories: " + fichePatient.getCalories());
            contentStream.newLineAtOffset(0, -20);
            contentStream.showText("Other: " + fichePatient.getOther());
            contentStream.endText();
            contentStream.close();

            document.save(fileName);
            document.close();
            System.out.println("PDF file generated successfully: " + fileName);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("PDF file generated successfully: " + fileName);
            alert.showAndWait();
        } catch (Exception e) {
            System.err.println("Error occurred while generating PDF file: " + e.getMessage());
        }
    }

}
