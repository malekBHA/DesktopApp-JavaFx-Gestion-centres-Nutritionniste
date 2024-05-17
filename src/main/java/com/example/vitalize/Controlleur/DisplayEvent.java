package com.example.vitalize.Controlleur;


import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.MFXToggleButton;
import io.github.palexdev.materialfx.utils.SwingFXUtils;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.util.Duration;
import com.example.vitalize.Entity.activite;
import com.example.vitalize.Entity.evenement;
import com.example.vitalize.Entity.participation;
import com.example.vitalize.Service.Activityservice;
import com.example.vitalize.Service.EmailService;
import com.example.vitalize.Service.EventService;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.List;

import javafx.scene.shape.SVGPath;


public class DisplayEvent implements Initializable {
    @FXML
    private MFXComboBox<String> Tri;
    @FXML

    private MFXTextField recherche;

    @FXML
    private ListView<evenement> ListEvents;
    @FXML
    private Label label1;
    @FXML
    private MFXButton Delete;
    @FXML
    private MFXButton Editer;
    @FXML
    private MFXButton Ajoutp;
    EventService es = new EventService();
    Activityservice as = new Activityservice();
    @FXML
    private MFXToggleButton particip;
    private Stage primaryStage;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        ListEvents.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(evenement Event, boolean empty) {
                super.updateItem(Event, empty);

                if (empty || Event == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    GridPane container = new GridPane();

                    TextFlow textFlow = new TextFlow();
                    String labelStyle = "-fx-fill: #9b8385;  -fx-font-size: 27  ;";
                    String nameStyle = "-fx-fill: #8b7080;  -fx-font-size: 40;";
                    String dataStyle = "-fx-fill: #9b8385; -fx-font-size: 20;";
                    Text nameData = new Text(Event.getNom() + "\n");
                    nameData.setStyle(nameStyle);
                    Text DescriptionText = new Text("Description: ");
                    DescriptionText.setStyle(labelStyle);
                    Text DescriptionData = new Text(Event.getDescription() + "\n");
                    DescriptionData.setStyle(dataStyle);
                    Text DureeText = new Text("Location: ");
                    DureeText.setStyle(labelStyle);
                    Text DureeData = new Text(Event.getLocalisation() + "\n");
                    DureeData.setStyle(dataStyle);
                    Text DateText = new Text("Date: ");
                    DateText.setStyle(labelStyle);
                    Text CapaText = new Text("Capacity: ");
                    CapaText.setStyle(labelStyle);
                    Text CapaTextd = new Text(String.valueOf(Event.getCapacite())+ "\n");
                    CapaTextd.setStyle(labelStyle);
                    Text DateData = new Text(Event.getDate() + "\n");
                    DateData.setStyle(dataStyle);
                    Text actText = new Text("Activities: ");
                    actText.setStyle(labelStyle);
                    Text orgText = new Text("Organisateur: ");
                    orgText.setStyle(labelStyle);
                    Text orgTextd = new Text(Event.getOrangisateur() + "\n");
                    orgTextd.setStyle(dataStyle);
                    Text actData = new Text(Event.getS() + "\n");
                    actData.setStyle(dataStyle);


                    String ImagePath = Event.getImage();
                    Image demonstrationImage = new Image(new File(ImagePath).toURI().toString());
                    ImageView imageView = new ImageView(demonstrationImage);
                    BufferedImage qrCodeImage = createQRImage(nameData.getText() + " \n" +
                                    DescriptionText.getText() + " \n" +
                                    DescriptionData.getText() + " \n" +
                                    DureeText.getText() + " \n" +
                                    DureeData.getText() + " min\n"+
                                    DateText.getText() + " \n" +
                                    DateData.getText()+ " \n"+
                                    actText.getText()+ " \n" +
                                    actData.getText()

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
                    ColumnConstraints col2 = new ColumnConstraints(420);
                    ColumnConstraints col3 = new ColumnConstraints(200);
                    container.getColumnConstraints().addAll(col1, col2, col3);


                    textFlow.getChildren().addAll(nameData, DescriptionText, DescriptionData, DureeText, DureeData,DateText,DateData,orgText,orgTextd,CapaText,CapaTextd,actText,actData);
                    container.add(imageView, 0, 0);
                    container.add(textFlow,1,0);
                    container.add(Qr, 2, 0);
                    ColumnConstraints columnConstraints = new ColumnConstraints();
                    columnConstraints.setHgrow(Priority.ALWAYS);
                    container.getColumnConstraints().addAll(columnConstraints, columnConstraints, columnConstraints);

                    container.setHgap(30);

                    setGraphic(container);

                    setGraphic(container);

                }
            }
        });
        List<participation> participations = new ArrayList<>(es.searchpart());
        for (evenement event : es.fetch()) {
            ListEvents.getItems().add(event);
        }
        String[] trier = { "Name","Organiser", "Capacity"};
        Tri.getItems().addAll(trier);

    }




    public void ajoutpass(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/vitalize/AddEvent.fxml"));
            Parent root = loader.load();

            Scene currentScene = ((Node) actionEvent.getSource()).getScene();

            currentScene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void refrech(ActionEvent actiondEvent) {
        ListEvents.getItems().setAll(es.fetch());
    }
    @FXML
    private void deleteSelectedactivity() {
        evenement selectedItem = ListEvents.getSelectionModel().getSelectedItem();

        if (selectedItem != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Confirmation");
            alert.setHeaderText("Delete selected event?");
            alert.setContentText("Are you sure you want to delete this event?");

            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    int id = selectedItem.getId();
                    es.delete(id);
                    ListEvents.getItems().remove(selectedItem);
                }
            });
        }
    }
    public void Editer(javafx.event.ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/vitalize/EditEvent.fxml"));
            loader.setControllerFactory(controllerClass -> {
                if (controllerClass == EditEvent.class) {
                    EditEvent editionactivityController = new EditEvent();
                    evenement selectedactivity = ListEvents.getSelectionModel().getSelectedItem();
                    int id = selectedactivity.getId();
                    editionactivityController.setPassedId(id);
                    return editionactivityController;
                } else {
                    return new EditEvent();
                }
            });

            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));

            stage.setOnHidden(event -> {
                updateListView();
            });

            stage.show();

            EditEvent editionactivityController = loader.getController();
            editionactivityController.setPrimaryStage(primaryStage);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void updateListView() {
        ListEvents.getItems().setAll(es.fetch());
    }



    public void tri(ActionEvent event) {
        if(Tri.getValue().equals("Name")) {
            java.util.List<evenement> EventList =  ListEvents.getItems().stream().sorted(Comparator.comparing(evenement::getNom)).toList();
            ObservableList<evenement> observableEventList = FXCollections.observableArrayList(EventList);
            ListEvents.setItems(observableEventList);
        }
        if(Tri.getValue().equals("Organiser")) {
            java.util.List<evenement> EventList =  ListEvents.getItems().stream().sorted(Comparator.comparing(evenement::getOrangisateur)).toList();
            ObservableList<evenement> observableEventList = FXCollections.observableArrayList(EventList);
            ListEvents.setItems(observableEventList);
        }
        if(Tri.getValue().equals("Capacity")) {
            java.util.List<evenement> EventList =  ListEvents.getItems().stream().sorted(Comparator.comparing(evenement::getCapacite)).toList();
            ObservableList<evenement> observableEventList = FXCollections.observableArrayList(EventList);
            ListEvents.setItems(observableEventList);
        }
    }

    public void ok(ActionEvent event) {
        List<evenement> EventList =  ListEvents.getItems().stream().filter(Event -> Event.getNom().contains(recherche.getText())).toList();
        ObservableList<evenement> observableEventList = FXCollections.observableArrayList(EventList);
        ListEvents.setItems(observableEventList);
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


    public void back(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/vitalize/DisplayActivity.fxml"));
            Parent root = loader.load();

            Scene currentScene = ((Node) actionEvent.getSource()).getScene();

            currentScene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void particip(MouseEvent mouseEvent) {
        particip.setDisable(true);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                particip.setDisable(false);
            }
        }, 30000);
        if(!particip.isSelected())
            es.deletep(ListEvents.getSelectionModel().getSelectedItem().getId());
        else{
            es.addp(new participation(150,ListEvents.getSelectionModel().getSelectedItem().getId()));
            EmailService ems=new EmailService();
            ems.sendEmail("aziz.chlif@esprit.tn","Event Participation","Your participation to the event "+ListEvents.getSelectionModel().getSelectedItem().getNom()+" is submitted");
        }


    }
    public void handleCellClick(javafx.scene.input.MouseEvent mouseEvent) {
        List<participation> participations = new ArrayList<>(es.searchpart());
        particip.setDisable(false);

        if(participations.stream().map(participation::getIdevent).anyMatch(c->c== ListEvents.getSelectionModel().getSelectedItem().getId())&&participations.stream().map(participation::getIduser).anyMatch(c->c==150))

        {
            particip.setSelected(true);
        }
        else{
            particip.setSelected(false);
        }


    }
}
