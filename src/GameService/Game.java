package GameService;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Deck.Card;
import Deck.Deck;
import Player.Player;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;
import sample.Flop;
import sample.GameRound;
import sample.PlayerUI;


public class Game extends Application {
    double dX;
    double dY;

    public static Pane appRoot = new Pane();
    public static Pane gameRoot;//информация игроков
    public static boolean flag = false;
    public static ArrayList<PlayerUI> playerUIS = new ArrayList<>();
    public static Stage st;
    public static ArrayList<ImageView> flopCards;




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
    private Button newApply;

    int X = 10;


    @FXML
    private ImageView card1;

    public static ImageView c1;

    public static ImageView c2;

    static ArrayList<ImageView> imageViews;

    public static Group group = new Group();

    @FXML
    void initialize() {
        System.out.println("initialize()");
        f1 = firstFlop;
        f2 = secondFlop;
        f3 = thirdFlop;
        flopCards = new ArrayList<>(3);
        flopCards.add(f1);
        flopCards.add(f2);
        flopCards.add(f3);

        accountScore = accountLabel;
        stakeSpin = stakeSpinner;
        bankLabel = bankScore;
        chat = chatArea;
        stakeLabel = myStakeLabel;
        applyButton = new Button();

        t1 = turn;
        r1 = river;
        c1 = card1;
        c2 = card2;
        Deck.generateDeck();
        Deck.shuffleCards();
        imageDrags(card1);
        imageDrags(card2);
        makeStake();


        newApply.setOnMouseClicked(e->{
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

    public void update(boolean flag){

    }

    @Override
    public void start(Stage stage) throws Exception {
        st = stage;

       Scene scene = new Scene(createContent());
       stage.setScene(scene);
       stage.show();
    }

    public Parent createContent(){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/sample/app.fxml"));
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        gameRoot = loader.getRoot();
        appRoot.getChildren().addAll(gameRoot);
        return appRoot;
    }

    public static PlayerUI createPlayer(String name){
        PlayerUI playerUI = new PlayerUI(name);
        playerUIS.add(playerUI);
        Platform.runLater(()->appRoot.getChildren().add(playerUI));
        Pane pane = new Pane();
        pane.getChildren().addAll(appRoot);
        Scene scene = new Scene(pane);
        Platform.runLater(()->st.setScene(scene));
        return playerUI;
    }

    public static void showCards(){
        String sIMG = filePathCard+"Diamonds"+"/"+"14"+".jpg";
        File fileCard = new File(sIMG);
        Image image = new Image(fileCard.toURI().toString());
        playerUIS.forEach(playerUI -> playerUI.setCards(image));
    }


}
