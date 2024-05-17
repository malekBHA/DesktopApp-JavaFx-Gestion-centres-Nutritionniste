package com.example.vitalize.Controlleur;

import com.google.zxing.client.j2se.MatrixToImageWriter;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.utils.SwingFXUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import com.example.vitalize.Service.EmailService;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import com.example.vitalize.Entity.UserSession;
import com.example.vitalize.Entity.Users;
import com.example.vitalize.Service.UserService;
import javafx.stage.Stage;
import com.example.vitalize.Entity.Commentaire;
import com.example.vitalize.Entity.Publication;
import com.example.vitalize.Entity.React;
import com.example.vitalize.Service.Servicecommentaire;
import com.example.vitalize.Service.Servicepublication;
import com.example.vitalize.Service.ServiceReact;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

import javax.imageio.ImageIO;
import javax.xml.stream.events.Comment;

public class ShowPublication implements Initializable {
    public TextField searchField;
    public MFXButton searchButton;
    public MFXButton addCommentButton;
    public Label label1;
    public MFXButton dislikeButton;
    public MFXButton likeButton;
    public ListView recommendedPublicationList;
    public MFXButton refreshButton;
    @FXML
    private ComboBox<String> typeComboBox;

    private Stage primaryStage;

    @FXML
    private ImageView qrCodeImageView;

    @FXML
    private MFXButton Ajoutp;

    @FXML
    private MFXButton Delete;

    @FXML
    private MFXButton Editer;

    @FXML
    private ListView<Publication> ListPublication;
    private ListView<Commentaire> commentaireListView;

    Servicepublication PublicationService = new Servicepublication();
    Servicecommentaire CommentaireService = new Servicecommentaire();

    @FXML
    private void handleRefreshButton(ActionEvent event) {

        updateListView();

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        int size = 125;

        // Configure ListPublication cell factory
        commentaireListView = new ListView<>();
        ListPublication.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Publication publication, boolean empty) {
                super.updateItem(publication, empty);

                if (empty || publication == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    GridPane container = new GridPane();
                    TextFlow textFlow = new TextFlow();

                    String imagePath = publication.getImage();
                    Image publicationImage = new Image(new File(imagePath).toURI().toString());
                    ImageView imageView = new ImageView(publicationImage);
                    imageView.setFitWidth(200);
                    imageView.setFitHeight(200);
                    ImageView qrCodeImageView = new ImageView();
                    Image qrCodeImage = displayQRCode(publication);
                    if (qrCodeImage != null) {
                        qrCodeImageView.setImage(qrCodeImage);
                        qrCodeImageView.setFitWidth(200);
                        qrCodeImageView.setFitHeight(200);
                    }

                    String labelStyle = "-fx-fill: #9b8385;  -fx-font-size: 27  ;";
                    String nameStyle = "-fx-fill: #8b7080;  -fx-font-size: 40;";
                    String dataStyle = "-fx-fill: #9b8385; -fx-font-size: 20;";

                    Text nameData = new Text("Titre: " + publication.getTitre() + "\n");
                    nameData.setStyle(nameStyle);

                    Text typeData = new Text("Type: " + publication.getType() + "\n");
                    typeData.setStyle(dataStyle);

                    Text descriptionData = new Text("Description: " + publication.getDescription() + "\n");
                    descriptionData.setStyle(dataStyle);

                    textFlow.getChildren().addAll(nameData, typeData, descriptionData);

                    container.add(imageView, 0, 0);
                    container.add(textFlow, 1, 0);
                    container.add(qrCodeImageView, 2, 0);
                    container.setHgap(30);

                    //ListView<Commentaire> commentaireListView = new ListView<>();
                    commentaireListView.setPrefSize(600, 100);

                    List<Commentaire> comments = PublicationService.getCommentsForPublication(publication.getId());

                    commentaireListView.setCellFactory(listView -> new ListCell<>() {
                        @Override
                        protected void updateItem(Commentaire commentaire, boolean empty) {
                            super.updateItem(commentaire, empty);

                            if (empty || commentaire == null) {
                                setText(null);
                            } else {
                                setText(commentaire.getContenu());
                            }
                        }
                    });
                    commentaireListView.getItems().clear();
                    commentaireListView.getItems().addAll(comments);
                    container.add(commentaireListView, 0, 1, 3, 1);

                    List<React> reacts = PublicationService.getAllReactsForPublication(publication.getId());
                    int totalLikes = 0;
                    int totalDislikes = 0;

                    for (React react : reacts) {
                        totalLikes += react.getLikeCount();
                        totalDislikes += react.getDislikeCount();
                    }

                    Label totalLikesLabel = new Label("Likes: " + totalLikes);
                    Label totalDislikesLabel = new Label("Dislikes: " + totalDislikes);

                    container.add(totalLikesLabel, 0, 2);
                    container.add(totalDislikesLabel, 1, 2);

                    setGraphic(container);
                }
            }
        });

        // Configure recommendedPublicationList cell factory
        // Configure recommendedPublicationList cell factory
        // Configure recommendedPublicationList cell factory
        recommendedPublicationList.setCellFactory(param -> new ListCell<Publication>() {
            @Override
            protected void updateItem(Publication publication, boolean empty) {
                super.updateItem(publication, empty);

                if (empty || publication == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    // Create a GridPane to hold the publication information
                    GridPane container = new GridPane();

                    // Create an ImageView for the publication image
                    ImageView imageView = new ImageView();
                    imageView.setFitWidth(200);
                    imageView.setFitHeight(200);

                    // Load the image from the publication's image path
                    String imagePath = publication.getImage();
                    if (imagePath != null && !imagePath.isEmpty()) {
                        Image publicationImage = new Image(new File(imagePath).toURI().toString());
                        imageView.setImage(publicationImage);
                    }

                    // Create Text objects for other attributes of the Publication
                    Text titreText = new Text("Titre: " + publication.getTitre());
                    Text typeText = new Text("Type: " + publication.getType());
                    Text descriptionText = new Text("Description: " + publication.getDescription());
                    // Add more Text objects for additional attributes as needed

                    // Style the Text objects as desired
                    String dataStyle = "-fx-fill: #9b8385; -fx-font-size: 20;";
                    titreText.setStyle(dataStyle);
                    typeText.setStyle(dataStyle);
                    descriptionText.setStyle(dataStyle);
                    // Add more styling as needed

                    // Add the ImageView and Text objects to the GridPane
                    container.add(imageView, 0, 0);
                    container.add(titreText, 0, 1);
                    container.add(typeText, 0, 2);
                    container.add(descriptionText, 0, 3);
                    // Add more Text objects to the GridPane as needed

                    // Set the GridPane as the graphic for the ListCell
                    setGraphic(container);
                }
            }
        });



        // Populate typeComboBox with distinct types from all publications
        ObservableList<String> types = FXCollections.observableArrayList();
        List<String> distinctTypes = PublicationService.getAllPublications().stream()
                .map(Publication::getType)
                .distinct()
                .collect(Collectors.toList());
        types.addAll(distinctTypes);
        typeComboBox.setItems(types);

        // Set listener for typeComboBox to filter publications by type
        typeComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            filterPublicationsByType(newValue);
        });

        typeComboBox.getSelectionModel().selectFirst();

        refreshButton.setOnAction(this::handleRefreshButton);
        updateListView();
    }



    @FXML
    private void handleCellClick(MouseEvent event) {

    }

    @FXML
    private void addCommentButtonClicked(ActionEvent event) {
        Publication selectedPublication = ListPublication.getSelectionModel().getSelectedItem();
        if (selectedPublication != null) {
            int publicationId = selectedPublication.getId();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/vitalize/AddCommentaire.fxml"));
            Parent root;
            try {
                root = loader.load();
                AddCommentaire addCommentaireController = loader.getController();
                addCommentaireController.populateFields(publicationId);

                Scene currentScene = ((Node) event.getSource()).getScene();
                currentScene.setRoot(root);
            } catch (IOException e) {
                e.printStackTrace();

            }
        }
    }

    public void addCommentaire(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/vitalize/AddCommentaire.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void likePublication(ActionEvent event) {

        Publication publication = ListPublication.getSelectionModel().getSelectedItem();
        if (publication != null) {

            UserSession userSession = UserSession.getInstance();
            Users user = userSession.getUser();
            if (user != null) {

                ServiceReact serviceReact = ServiceReact.getInstance();

                React existingReact = serviceReact.getReactByUserAndPublication(user, publication);
                if (existingReact != null) {

                    if (existingReact.getLikeCount() > 0) {

                        existingReact.setLikeCount(0);
                    } else {

                        existingReact.setLikeCount(1);
                    }

                    serviceReact.edit(existingReact);
                } else {

                    React newReact = new React();
                    newReact.setUser(user);
                    newReact.setPublication(publication);
                    newReact.setLikeCount(1);

                    serviceReact.add(newReact);
                }

                Servicepublication publicationService = new Servicepublication();
                List<Publication> recommendedPublications = publicationService.getRecommendedPublications(publication.getType());

                recommendedPublicationList.getItems().setAll(recommendedPublications);
            }
        }
    }




    @FXML
    private void dislikePublication(ActionEvent event) {
        Publication publication = ListPublication.getSelectionModel().getSelectedItem();
        if (publication != null) {
            UserSession userSession = UserSession.getInstance();
            Users user = userSession.getUser();
            if (user != null) {

                ServiceReact serviceReact = ServiceReact.getInstance();
                React existingReact = serviceReact.getReactByUserAndPublication(user, publication);
                if (existingReact != null) {

                    if (existingReact.getDislikeCount() > 0) {

                        existingReact.setDislikeCount(0);
                    } else {

                        existingReact.setDislikeCount(1);

                        if (existingReact.getLikeCount() > 0) {
                            existingReact.setLikeCount(0);
                        }
                    }

                    serviceReact.edit(existingReact);
                } else {

                    React newReact = new React();
                    newReact.setUser(user);
                    newReact.setPublication(publication);
                    newReact.setDislikeCount(1);

                    serviceReact.add(newReact);
                }

            }
        }
    }



    private void closeAndReopenWindow() {
        // Close the current window
        Stage stage = (Stage) ListPublication.getScene().getWindow();
        stage.close();

        // Open a new window
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ShowPublication.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage newStage = new Stage();
            newStage.setScene(scene);
            newStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void filterPublicationsByType(String type) {
        if (type == null || type.isEmpty()) {
            updateListView();
        } else {
            List<Publication> filteredPublications = PublicationService.getAllPublications()
                    .stream()
                    .filter(publication -> publication.getType().equals(type))
                    .collect(Collectors.toList());
            ListPublication.getItems().setAll(filteredPublications);
        }
    }

    public void deleteSelectedPublication(ActionEvent event) {

        Publication SP = ListPublication.getSelectionModel().getSelectedItem();

        if (SP != null) {
            int id = SP.getId();
            PublicationService.delete(id);
            ListPublication.getItems().remove(SP);
        }
    }

    public void Editer(javafx.event.ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/vitalize/EditPublication.fxml"));
            loader.setControllerFactory(controllerClass -> {
                if (controllerClass == EditPublication.class) {
                    EditPublication editionController = new EditPublication();
                    Publication selectedExercice = ListPublication.getSelectionModel().getSelectedItem();
                    int id = selectedExercice.getId();
                    editionController.setPassedId(id);
                    return editionController;
                } else {
                    return new EditPublication();
                }
            });

            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));

            stage.setOnHidden(event -> {
                updateListView();
            });

            stage.show();

            EditPublication editionExerciceController = loader.getController();
            editionExerciceController.setPrimaryStage(primaryStage);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateListView() {
        ListPublication.getItems().setAll(PublicationService.fetch());
    }

    public void ajoutpass(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/vitalize/AddPublication.fxml"));
            Parent root = loader.load();

            Scene currentScene = ((Node) event.getSource()).getScene();

            currentScene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to generate QR code
    private byte[] generateQRCode(String data, int width, int height) throws WriterException, IOException {
        Map<EncodeHintType, ErrorCorrectionLevel> hintMap = new HashMap<>();
        hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, width, height, hintMap);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", byteArrayOutputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return byteArrayOutputStream.toByteArray();
    }

    @FXML
    public void searchByTitle() {

        String query = searchField.getText().trim().toLowerCase();


        List<Publication> allPublications = PublicationService.getAllPublications();


        List<Publication> filteredPublications = allPublications.stream()
                .filter(publication -> publication.getTitre().toLowerCase().contains(query))
                .collect(Collectors.toList());


        ListPublication.getItems().clear();

        ListPublication.getItems().addAll(filteredPublications);
    }

    private Image displayQRCode(Publication publication) {
        String publicationData = getPublicationData(publication);
        try {
            BufferedImage qrCodeImage = createQRImage(publicationData, 200);
            if (qrCodeImage != null) {
                return SwingFXUtils.toFXImage(qrCodeImage, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; // Return null in case of failure
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


    private String getPublicationData(Publication publication) {
        StringBuilder data = new StringBuilder();

        // Append publication details to the data string
        data.append("Title: ").append(publication.getTitre()).append("\n");
        data.append("Type: ").append(publication.getType()).append("\n");
        data.append("Description: ").append(publication.getDescription()).append("\n");

        return data.toString();
    }

    public void EditCommentaire(javafx.event.ActionEvent actionEvent) {
        try {


            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/vitalize/EditCommentaire.fxml"));
            loader.setControllerFactory(controllerClass -> {
                if (controllerClass == EditCommentaire.class) {
                    EditCommentaire editionController = new EditCommentaire();
                    Commentaire selectedCommentaire = commentaireListView.getSelectionModel().getSelectedItem();
                    int id = selectedCommentaire.getId();
                    editionController.setPassedId(id);
                    return editionController;
                } else {
                    return new EditCommentaire();
                }
            });

            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));

            stage.setOnHidden(event -> {
                updateListView();
            });

            stage.show();

            EditCommentaire editionCommentaireController = loader.getController();
            editionCommentaireController.setPrimaryStage(primaryStage);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteSelectedCommentaire(ActionEvent event) {

        Commentaire SC = commentaireListView.getSelectionModel().getSelectedItem();

        if (SC != null) {
            int id = SC.getId();
            CommentaireService.delete(id);
            ListPublication.getItems().remove(SC);
        }
    }




}
