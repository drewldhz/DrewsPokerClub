package sample;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.io.File;
import java.util.ArrayList;


public class PlayerUI extends Pane {
    public static Label statusLabel;
    public static Label scoreLabel;
    public static ImageView card1 = new ImageView();
    public static ImageView card2 = new ImageView();
    ArrayList<ImageView> hand = new ArrayList<>(2);
    private File fileCard = new File("C:/ShitCode/DrewsPokerClub/src/assets/Deck/" + "back.jpg");
    private Image image = new Image(fileCard.toURI().toString());
    public int x = 0;
    public PlayerUI(){
        hand.add(card1);
        hand.add(card2);

        statusLabel = new Label("ждет действий...");
        statusLabel.setTextFill(Color.YELLOW);

        scoreLabel = new Label("10000");
        scoreLabel.setTextFill(Color.YELLOW);

        hand.forEach(imageView ->{
            imageView.setImage(image);
            imageView.setFitHeight(93);
            imageView.setFitWidth(60);
            imageView.setX(900-x);
            imageView.setY(500);

            x+=60;
            getChildren().add(imageView);
        });

        statusLabel.setTranslateX(900);
        statusLabel.setTranslateY(450);
        scoreLabel.setTranslateX(900);
        scoreLabel.setTranslateY(475);

        getChildren().add(statusLabel);
        getChildren().add(scoreLabel);
    }
    public void setLabelText(String s){
        Platform.runLater(()-> statusLabel.setText(s));
    }

    public void setCards(Image image){
        hand.forEach(imageView -> imageView.setImage(image));
    }


}
