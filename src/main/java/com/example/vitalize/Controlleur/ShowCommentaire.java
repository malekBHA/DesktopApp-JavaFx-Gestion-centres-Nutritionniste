package com.example.vitalize.Controlleur;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.utils.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import com.example.vitalize.Entity.Commentaire;
import com.example.vitalize.Service.Servicepublication;
import com.example.vitalize.Service.Servicecommentaire;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ShowCommentaire implements Initializable {
    private Stage primaryStage;

    @FXML
    private MFXButton AjoutCommentaire;

    @FXML
    private MFXButton DeleteCommentaire;

    @FXML
    private MFXButton EditerCommentaire;

    @FXML
    private ListView<Commentaire> ListCommentaire;

    private final Servicecommentaire commentaireService = new Servicecommentaire();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        int imageSize = 125;

        ListCommentaire.setCellFactory(param -> new ListCell<>() {

            @Override
            protected void updateItem(Commentaire commentaire, boolean empty) {
                super.updateItem(commentaire, empty);

                if (empty || commentaire == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    GridPane container = new GridPane();

                    TextFlow textFlow = new TextFlow();


                    Text contenuData = new Text("Contenu: " + commentaire.getContenu() + "\n");

                    textFlow.getChildren().addAll(contenuData);

                    container.add(textFlow, 0, 0);  // Add textFlow to column 0

                    setGraphic(container);
                }
            }
        });

        ListCommentaire.getItems().addAll(commentaireService.fetch());
    }

    public void deleteSelectedCommentaire(ActionEvent event) {
        Commentaire selectedCommentaire = ListCommentaire.getSelectionModel().getSelectedItem();

        if (selectedCommentaire != null) {
            int id = selectedCommentaire.getId();
            commentaireService.delete(id);
            ListCommentaire.getItems().remove(selectedCommentaire);
        }
    }

    public void editCommentaire(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/vitalize/EditCommentaire.fxml"));
            loader.setControllerFactory(controllerClass -> {
                if (controllerClass == EditCommentaire.class) {
                    EditCommentaire editionController = new EditCommentaire();
                    Commentaire selectedCommentaire = ListCommentaire.getSelectionModel().getSelectedItem();
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


    public void ajoutCommentaire(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/vitalize/AddCommentaire.fxml"));
            Parent root = loader.load();

            Scene currentScene = ((Node) event.getSource()).getScene();

            currentScene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void updateListView() {
        ListCommentaire.getItems().setAll(commentaireService.fetch());
    }
    public void handleCellClick(MouseEvent mouseEvent) {
    }
}



