package com.example.vitalize.Controlleur;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.utils.SwingFXUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;
import java.util.ResourceBundle;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.example.vitalize.Entity.activite;
import com.example.vitalize.Service.Activityservice;

public class DisplayActivity implements Initializable{
    @FXML
    private MFXComboBox<String> Tri;
    @FXML

    private MFXTextField recherche;
    @FXML
    private ListView<activite> ListActivities;
    @FXML
    private Label label1;
    @FXML
    private MFXButton Delete;
    @FXML
    private MFXButton Editer;
    @FXML
    private MFXButton Ajoutp;
    Activityservice as = new Activityservice();
    private Stage primaryStage;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ListActivities.setCellFactory(param -> new ListCell<>() {

            @Override
            protected void updateItem(activite activite, boolean empty) {
                super.updateItem(activite, empty);

                if (empty || activite == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    GridPane container = new GridPane();

                    TextFlow textFlow = new TextFlow();


                    String labelStyle = "-fx-fill: #9b8385;  -fx-font-size: 27  ;";
                    String nameStyle = "-fx-fill: #8b7080;  -fx-font-size: 40;";

                    String dataStyle = "-fx-fill: #9b8385; -fx-font-size: 20;";


                    Text nameData = new Text(activite.getType_activite() + "\n");
                    nameData.setStyle(nameStyle);

                    Text DescriptionText = new Text("Description: ");
                    DescriptionText.setStyle(labelStyle);

                    Text DescriptionData = new Text(activite.getDescription() + "\n");
                    DescriptionData.setStyle(dataStyle);

                    Text DureeText = new Text("Duree: ");
                    DureeText.setStyle(labelStyle);
                    Text DureeData = new Text(activite.getDuree() + "min\n");
                    DureeData.setStyle(dataStyle);





                    String ImagePath = activite.getImage();
                    Image demonstrationImage = new Image(new File(ImagePath).toURI().toString());
                    ImageView imageView = new ImageView(demonstrationImage);
                    BufferedImage qrCodeImage = createQRImage(nameData.getText() + " \n" +
                                    DescriptionText.getText() + " \n" +
                                    DescriptionData.getText() + " \n" +
                                    DureeText.getText() + " \n" +
                                    DureeData.getText() + " min\n"

                            , 125);
                    Image qrCodeImageFX = SwingFXUtils.toFXImage(qrCodeImage, null);
                    ImageView Qr = new ImageView(qrCodeImageFX);
                    imageView.setFitHeight(200);
                    imageView.setFitWidth(200);
                    nameData.setWrappingWidth(200);
                    DescriptionData.setWrappingWidth(200);
                    DescriptionText.setWrappingWidth(200);
                    DureeText.setWrappingWidth(200);
                    DureeData.setWrappingWidth(200);
                    ColumnConstraints col1 = new ColumnConstraints(200);
                    ColumnConstraints col2 = new ColumnConstraints(450);
                    ColumnConstraints col3 = new ColumnConstraints(200);
                    container.getColumnConstraints().addAll(col1, col2, col3);


                    textFlow.getChildren().addAll(nameData, DescriptionText, DescriptionData, DureeText, DureeData);
                    container.add(textFlow, 1, 0);
                    container.add(imageView, 0, 0);
                    container.add(Qr, 2, 0);
                    ColumnConstraints columnConstraints = new ColumnConstraints();
                    columnConstraints.setHgrow(Priority.ALWAYS);
                    container.getColumnConstraints().addAll(columnConstraints, columnConstraints, columnConstraints);

                    container.setHgap(30);

                    setGraphic(container);

                }
            }
        });

        ListActivities.getItems().addAll(as.fetch());
        String[] trier = { "Type","Description", "Duree"};
        Tri.getItems().addAll(trier);

    }

    public BufferedImage createQRImage( String qrCodeText, int size)
    {
        Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<>();
        hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix byteMatrix = null;
        try {
            byteMatrix = qrCodeWriter.encode(qrCodeText, BarcodeFormat.QR_CODE, size, size, hintMap);
        } catch (WriterException e) {
            throw new RuntimeException(e);
        }
        int matrixWidth = byteMatrix.getWidth();
        BufferedImage image = new BufferedImage(matrixWidth, matrixWidth, BufferedImage.TYPE_INT_RGB);
        image.createGraphics();

        Graphics2D graphics = (Graphics2D) image.getGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, matrixWidth, matrixWidth);
        graphics.setColor(Color.BLACK);

        for (int i = 0; i < matrixWidth; i++) {
            for (int j = 0; j < matrixWidth; j++) {
                if (byteMatrix.get(i, j)) {
                    graphics.fillRect(i, j, 1, 1);
                }
            }
        }
        return image;
    }
 

    public void ajoutpass(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/vitalize/AddActivity.fxml"));
            Parent root = loader.load();

            Scene currentScene = ((Node) actionEvent.getSource()).getScene();

            currentScene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void back(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/vitalize/DisplayEvent.fxml"));
            Parent root = loader.load();

            Scene currentScene = ((Node) actionEvent.getSource()).getScene();

            currentScene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void refrech(ActionEvent actionEvent) {
        ListActivities.getItems().setAll(as.fetch());
    }
    @FXML
    private void deleteSelectedactivity() {
        activite selectedItem = ListActivities.getSelectionModel().getSelectedItem();

        if (selectedItem != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Confirmation");
            alert.setHeaderText("Delete selected activity?");
            alert.setContentText("Are you sure you want to delete this activity?");

            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    int id = selectedItem.getId();
                    as.delete(id);
                    ListActivities.getItems().remove(selectedItem);
                }
            });
        }
    }
    public void Editer(javafx.event.ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/vitalize/EditActivity.fxml"));
            loader.setControllerFactory(controllerClass -> {
                if (controllerClass == EditActivity.class) {
                    EditActivity editionactivityController = new EditActivity();
                    activite selectedactivity = ListActivities.getSelectionModel().getSelectedItem();
                    int id = selectedactivity.getId();
                    editionactivityController.setPassedId(id);
                    return editionactivityController;
                } else {
                    return new EditActivity();
                }
            });

            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));

            stage.setOnHidden(event -> {
                updateListView();
            });

            stage.show();

            EditActivity editionactivityController = loader.getController();
            editionactivityController.setPrimaryStage(primaryStage);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void updateListView() {
        ListActivities.getItems().setAll(as.fetch());
    }

    public void handleCellClick(MouseEvent mouseEvent) {
    }

    public void tri(ActionEvent event) {
        if(Tri.getValue().equals("Type")) {
            List<activite> activiteList =  ListActivities.getItems().stream().sorted(Comparator.comparing(activite::getType_activite)).toList();
            ObservableList<activite> observableactiviteList = FXCollections.observableArrayList(activiteList);
            ListActivities.setItems(observableactiviteList);
        }
        if(Tri.getValue().equals("Duree")) {
            List<activite> activiteList =  ListActivities.getItems().stream().sorted(Comparator.comparing(activite::getDuree)).toList();
            ObservableList<activite> observableactiviteList = FXCollections.observableArrayList(activiteList);
            ListActivities.setItems(observableactiviteList);
        }
        if(Tri.getValue().equals("Description")) {
            List<activite> activiteList =  ListActivities.getItems().stream().sorted(Comparator.comparing(activite::getDescription)).toList();
            ObservableList<activite> observableactiviteList = FXCollections.observableArrayList(activiteList);
            ListActivities.setItems(observableactiviteList);
        }
    }

    public void ok(ActionEvent event) {
        List<activite> activiteList =  ListActivities.getItems().stream().filter(activite -> activite.getType_activite().contains(recherche.getText())).toList();
        ObservableList<activite> observableactiviteList = FXCollections.observableArrayList(activiteList);
        ListActivities.setItems(observableactiviteList);
    }

}