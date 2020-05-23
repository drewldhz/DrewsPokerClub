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

public class Main extends Application  {
   
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Holdem Poker");

        primaryStage.setScene(new Scene(root, 700, 400));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
        //CheckCombinations checkCombinations = new CheckCombinations();
        //ArrayList<Card> cards = new ArrayList<>();
        //ArrayList<Card> hands = new ArrayList<>();
        //cards.add(new Hearts("10"));
        //cards.add(new Diamonds("10"));
        //cards.add(new Spades("10"));
        //cards.add(new Clubs("10"));
        //cards.add(new Hearts("13"));
        //hands.add(new Hearts("14"));
        //hands.add(new Hearts("7"));
        ////checkCombinations.checkSequence(cards);
        //hands.addAll(cards);
        //checkCombinations.checkSeq(hands);

    }


}
