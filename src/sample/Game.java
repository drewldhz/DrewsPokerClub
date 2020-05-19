package sample;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;


public class Game {
    double dX;
    double dY;
    int iNumCard = 2;
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ImageView card2;

    @FXML
    private ImageView firstFlop;

    public static ImageView f1;

    @FXML
    private ImageView secondFlop;

    public static ImageView f2;

    @FXML
    private ImageView thirdFlop;

    public static ImageView f3;

    @FXML
    private ImageView turn;

    public static ImageView t1;

    @FXML
    private ImageView river;

    public static ImageView r1;

    @FXML
    private Slider stakeSpinner;
    public static Slider stakeSpin;

    @FXML
    private TextArea chatArea;

    public static TextArea chat;

    @FXML
    private Button applyButton;

    @FXML
    private Button fauldButton;

    @FXML
    private Label enemyStakeRight;

    @FXML
    private Label myStakeLabel;

    String filePathCard = "C:/ShitCode/firstUI/src/assets/Deck/";

    public static int iStake = 0;

    public static boolean bReadyStake = false;

    public static boolean fauld = false;

    @FXML
    private Label accountLabel;

    public static Label accountScore;

    @FXML
    private Label bankScore;

    public static Label bankLabel;



    @FXML
    private ImageView card1;

    public static ImageView c1;

    public static ImageView c2;

    @FXML
    void initialize() {
        accountScore = accountLabel;
        stakeSpin = stakeSpinner;
        bankLabel = bankScore;
        chat = chatArea;
        //chatArea.setText("Hello");
        f1 = firstFlop;
        f2 = secondFlop;
        f3 = thirdFlop;
        t1 = turn;
        r1 = river;
        c1 = card1;
        c2 = card2;
        Deck.generateDeck();
        Deck.shuffleCards();
        imageDrags(card1);
        imageDrags(card2);
        makeStake();

        applyButton.setOnMouseClicked(e->{
            /*if(stakeSpinner.getValue()>=Player.maxBid)*/bReadyStake = true;
        });

        fauldButton.setOnMouseClicked(e->{
            //fauld = true;
            iStake = -1;
            accountScore.setText(String.valueOf(Player.acc));
            bReadyStake = true;
        });

    }

    void imageDrags(ImageView obj){
        obj.setOnMousePressed(e->{
            dX = obj.getTranslateX()-e.getSceneX();
            dY = obj.getTranslateY()-e.getSceneY();
        });
        obj.setOnMouseDragged(e->{
            obj.setTranslateX(dX+e.getSceneX());
            obj.setTranslateY(dY+e.getSceneY());
        });

    }

    void extractCards(){
            ArrayList<ImageView> cards = new ArrayList<>();
            cards.add(card1);
            cards.add(card2);
            for(ImageView imageCard: cards){
                Card card = Deck.extractCard();
                //System.out.println(filePathCard+card.SUIT+"/"+card.rate+".jpg");
                File fileCard = new File(filePathCard+card.SUIT+"/"+card.rate+".jpg");
                imageCard.setImage(new Image(fileCard.toURI().toString()));
            }
    }



    void extractFlop(){
        ArrayList<ImageView> cards = new ArrayList<>();
        cards.add(firstFlop);
        cards.add(secondFlop);
        cards.add(thirdFlop);
        for(ImageView imageCard: cards){
            Card card = Deck.extractCard();
            //System.out.println(filePathCard+card.SUIT+"/"+card.rate+".jpg");
            File fileCard = new File(filePathCard+card.SUIT+"/"+card.rate+".jpg");
            imageCard.setImage(new Image(fileCard.toURI().toString()));
            /*FadeTransition ft = new FadeTransition();
            ft.setNode(imageCard);
            ft.setDuration(Duration.millis(2000));
            ft.setFromValue(1.0);
            ft.setToValue(0.0);
            ft.setCycleCount(2);
            ft.setAutoReverse(true);
            ft.play();*/
        }
        System.out.println(Deck.shuffledDeck.size()+" карт осталось в колоде");
    }

   public void makeStake(){
        // Adding Listener to value property.
        stakeSpinner.valueProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> changed, Number oldValue, Number newValue){
                    myStakeLabel.setText(String.valueOf(Math.round(newValue.doubleValue())));
                    iStake = Integer.parseInt(myStakeLabel.getText());
                    //System.out.println(iStake+" value from slider");
                    //if(iStake<Player.maxBid){
                    //    applyButton.setDisable(true);
                    //}
                    //else applyButton.setDisable(false);
                    if(iStake==stakeSpinner.getMin()){
                        applyButton.setText("Call");
                    }
                    else  if(iStake>stakeSpinner.getMin()){
                        applyButton.setText("raise");
                    }
                }
            });
    }

    public static void vSetAccount(int acc){
        Platform.runLater(()->accountScore.setText(String.valueOf(acc)));
    }

    public static void vSetBank(int value){
        Platform.runLater(()->bankLabel.setText(String.valueOf(value)));
    }




}
