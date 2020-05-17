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

public class Main extends Application implements EventHandler<ActionEvent> {
    Button button;
    double dX;
    double dY;
    @Override
    public void start(Stage primaryStage) throws Exception{
        Circle circle = new Circle(10,10, 150, Color.YELLOW);
        circle.setOnMousePressed(e->{
            dX = circle.getTranslateX()-e.getSceneX();
            dY= circle.getTranslateY()-e.getSceneY();
        });
        circle.setOnMouseDragged(e->{
            circle.setTranslateX(dX+e.getSceneX());
            circle.setTranslateY(dY+e.getSceneY());
        });
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Holdem Poker");
        Pane r = new Pane();
        r.getChildren().add(circle);


        primaryStage.setScene(new Scene(root, 700, 400));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        if(actionEvent.getSource()==button){
            System.out.println("start code");
        }
    }
}
