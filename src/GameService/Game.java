package GameService;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Deck.Card;
import Deck.Deck;
import Player.Player;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
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

    public static Label stakeLabel;

    static String filePathCard = "C:/ShitCode/DrewsPokerClub/src/assets/Deck/";

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

    static ArrayList<ImageView> imageViews;

    @FXML
    void initialize() {


        accountScore = accountLabel;
        stakeSpin = stakeSpinner;
        bankLabel = bankScore;
        chat = chatArea;
        stakeLabel = myStakeLabel;

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
            if(stakeSpinner.getValue()>=stakeSpinner.getMin())bReadyStake = true;
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


   public void makeStake(){
        // Adding Listener to value property.
        stakeSpinner.valueProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> changed, Number oldValue, Number newValue){
                    myStakeLabel.setText(String.valueOf(Math.round(newValue.doubleValue())));
                    iStake = Integer.parseInt(myStakeLabel.getText());
                    if(iStake==stakeSpinner.getMin()&&stakeSpinner.getMin()!=0)applyButton.setText("Call");
                    else  if(iStake>stakeSpinner.getMin())applyButton.setText("raise");
                    else  if(iStake==stakeSpinner.getMin()&&stakeSpinner.getMin()==0)applyButton.setText("Check");
                }
            });
    }

    public static void vSetAccount(int acc){
        Platform.runLater(()->accountScore.setText(String.valueOf(acc)));
    }

    public static void vSetBank(int value){
        Platform.runLater(()->bankLabel.setText(String.valueOf(value)));
    }

    public static void vSetSpinLabel( int value){
        Platform.runLater(()->stakeLabel.setText(String.valueOf(value)));
    }

    public static void vAnimateValidation(ArrayList<Card> cards){
        imageViews = new ArrayList<>();
        imageViews.add(c1);
        imageViews.add(c2);
        imageViews.add(f1);
        imageViews.add(f2);
        imageViews.add(f3);
        imageViews.add(t1);
        imageViews.add(r1);
        for (Card card: cards){
            String sIMG = filePathCard+card.SUIT+"/"+card.rate+".jpg";
            for(ImageView imageView: imageViews){
               if(imageView.getImage().getUrl().substring(6).equals(sIMG)){
                   FadeTransition ft = new FadeTransition();
                   ft.setNode(imageView);
                   ft.setDuration(Duration.millis(2000));
                   ft.setFromValue(1.0);
                   ft.setToValue(0.0);
                   ft.setCycleCount(10 );
                   ft.setAutoReverse(true);
                   ft.play();
               }
           }
        }
    }


}