package sample;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import GameService.GameService;
import Player.Player;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import GameService.Game;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Controller {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button startGameButton;

    @FXML
    private Button connectButton;

    private static Parent root;

    @FXML
    void initialize() {
        startGameButton.setOnAction(actionEvent -> {
            startGameButton.getScene().getWindow().hide();
            Thread server = new Thread(new GameService());


            Game game = new Game();
            try {
                game.start(new Stage());
            } catch (Exception e) {
                e.printStackTrace();
            }


            Thread player = null;
            try {
                player = new Thread(new Player("player1"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            player.start();


        });

        connectButton.setOnAction(actionEvent -> {

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/sample/app.fxml"));
            try {
                loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            root = loader.getRoot();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
            //stage.showAndWait();
            Thread player = null;
            try {
                player = new Thread(new Player("Server"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            player.start();
        });


    }
}

