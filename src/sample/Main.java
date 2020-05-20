package sample;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.util.ArrayList;

public class Main extends Application implements EventHandler<ActionEvent> {
    Button button;
    double dX;
    double dY;
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Holdem Poker");

        primaryStage.setScene(new Scene(root, 700, 400));
        primaryStage.show();
    }


    public static void main(String[] args) {
        //launch(args);
        CheckCombinations checkCombinations = new CheckCombinations();
        ArrayList<Card> cards = new ArrayList<>();
        cards.add(new Clubs("3"));
        cards.add(new Hearts("3"));
        cards.add(new Spades("3"));
        cards.add(new Clubs("4"));
        cards.add(new Diamonds("4"));
        checkCombinations.checkSequence(cards);
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        if(actionEvent.getSource()==button){
            System.out.println("start code");
        }
    }
}
