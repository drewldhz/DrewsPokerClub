package Player;

import Deck.*;
import GameService.SignalToCreatePlayer;
import javafx.application.Platform;
import javafx.scene.image.Image;
import GameService.Game;
import GameService.GameInfo;
import sample.Flop;
import sample.PlayerUI;
import sample.River;
import sample.Turn;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Player extends Thread{
    public static int iHandSize = 2;
    public ArrayList<Card> hand = new ArrayList<>(iHandSize);
    public ArrayList<Card> cardsOnTable = new ArrayList<>(5);
    public static String nickName = "Server";
    private String name;
    private int account = 10;
    volatile public static int acc = 10;
    //To do: make enum
    private final String requestBid = "your bid mr.";
    String filePathCard = "C:/ShitCode/DrewsPokerClub/src/assets/Deck/";
    Socket socket;
    RoundStakes myBid = new RoundStakes(0, name);
    public static int maxBid = 0;
    public int callBid = 0;
    public int bid = 0;
    public int bankScore = 0;
    static public Map<String, PlayerUI> mapPlayersUI = new HashMap<>();


    public static int counter = 0;

    public Player(String name) throws IOException
    {
        vSetName(name);
        Game.stakeSpin.setMax(account);
    }


    public void vSetName(String name) {
        this.name = name;
    }

    @Override
    public void run() {

        System.out.println(socket);
        try {
            socket = new Socket(InetAddress.getLocalHost(), 10000);
            SendResponseToGameService(nickName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        int iCountCards = 0;
        while (true){

            Game.vSetAccount(account);
            Object receivedMessage = null;
            try {
                receivedMessage = receiveMessage();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            //Create players
            if(receivedMessage!=null && receivedMessage instanceof SignalToCreatePlayer){
                SignalToCreatePlayer signalToCreatePlayer = (SignalToCreatePlayer) receivedMessage;
                System.out.println(signalToCreatePlayer.name+"LLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLL");
                mapPlayersUI.put(signalToCreatePlayer.name,Game.createPlayer(signalToCreatePlayer.name));

            }
            //Receive cards
            if(receivedMessage!=null && receivedMessage instanceof String && receivedMessage.equals("send cards")){
                Game.chat.appendText("Раздача карт"+"\n");//System.out.println(name +" "+ receivedMessage);
                Card card = null;
                try {
                    card = receiveCard();
                    hand.add(card);
                    //System.out.println(name + " принял карту : " + vCheckInstance(card) + ", карт в руке " + hand.size());
                    Game.chat.appendText(name + " принял карту : " + vCheckInstance(card)+"\n");
                    File fileCard = new File(filePathCard + card.SUIT + "/" + card.rate + ".jpg");
                    if (iCountCards == 0) Game.c1.setImage(new Image(fileCard.toURI().toString()));
                    System.out.println(filePathCard + card.SUIT + "/" + card.rate + ".jpg");
                    if (iCountCards == 1) Game.c2.setImage(new Image(fileCard.toURI().toString()));
                    iCountCards++;
                    if(iCountCards>1) iCountCards = 0;

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
            //Open flop
            if(receivedMessage!=null && receivedMessage instanceof Flop){
                Flop flop = (Flop) receivedMessage;
                if(!flop.getCardsFlop().isEmpty()){
                    flop.getCardsFlop().forEach(card -> {
                        Game.chat.appendText("карта на flop : " + vCheckInstance(card)+"\n");
                        File fileCard = new File(filePathCard + card.SUIT + "/" + card.rate + ".jpg");
                        cardsOnTable.add(card);
                        Game.flopCards.get(cardsOnTable.size()-1).setImage(new Image(fileCard.toURI().toString()));
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    });
                }
                else Game.chat.appendText("FLOP IS EMPTY!");
            }
            //Open turn
            if(receivedMessage!=null && receivedMessage instanceof Turn) {
                //System.out.println(name +" "+ receivedMessage);
                Card card = ((Turn) receivedMessage).getTurnCard();
                Game.chat.appendText("карта на turn : " + vCheckInstance(card)+"\n");
                File fileCard = new File(filePathCard + card.SUIT + "/" + card.rate + ".jpg");
                cardsOnTable.add(card);
                if (cardsOnTable.size() == 4) Game.t1.setImage(new Image(fileCard.toURI().toString()));
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            //Open river
            if(receivedMessage!=null && receivedMessage instanceof River) {
                //System.out.println(name +" "+ receivedMessage);
                Card card = ((River) receivedMessage).getRiverCard();
                Game.chat.appendText("карта на river : " + vCheckInstance(card)+"\n");
                File fileCard = new File(filePathCard + card.SUIT + "/" + card.rate + ".jpg");
                cardsOnTable.add(card);
                if (cardsOnTable.size() == 5) Game.r1.setImage(new Image(fileCard.toURI().toString()));
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            //Request Bid
            if(receivedMessage!=null && receivedMessage instanceof RoundStakes/* && receivedMessage.equals(requestBid)*/){
                System.out.println("функция ставок");
                RoundStakes roundStakes = (RoundStakes)receivedMessage;

                maxBid = roundStakes.getRate();
                bankScore = roundStakes.bank;
                Game.vSetBank(bankScore);
                callBid = maxBid-bid;
                Game.stakeSpin.setMin(callBid);
                Game.chat.appendText(requestBid+" "+name+" ?"+"\n");
                while(true){
                    System.out.println(Game.bReadyStake+" ! ");
                    if(Game.bReadyStake&&Game.iStake>=callBid) {
                        System.out.println(Game.iStake+" Ставка сделана");
                        break;
                    }else if(Game.bReadyStake&&Game.iStake<0){
                        System.out.println(Game.iStake+" fauld");
                        break;
                    }
                }
                Integer bidResponse = Game.iStake;

                bid += bidResponse;
                account=account-Game.iStake;


                Game.chat.appendText(bid+"\n");
                makeStake(bid);//bidResponse
                Game.bReadyStake = false;
            }


            //Receive Game information
            if(receivedMessage!=null && receivedMessage instanceof GameInfo){
                System.out.println("RESET");
                maxBid = 0;
                callBid = 0;
                bid = 0;
                bankScore = 0;
                Game.vSetBank(0);
                Game.stakeSpin.setMin(0);

            }

            if(receivedMessage!=null && receivedMessage instanceof CheckCombinations){
                System.out.println("Проверка комбинаций");
                try {
                    SendResponseToGameService(new PlayerHand(hand));
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                CheckCombinations check = new CheckCombinations();
                hand.addAll(cardsOnTable);
                int iPower = check.checkSeq(hand);
                System.out.println("Power is "+iPower);
                //SendResponseToGameService(Integer.valueOf(iPower));
            }

            System.out.println(name+ " ждет дальнейших действий...");
        }



    }


    public Card receiveCard() throws IOException, ClassNotFoundException
    {
        //socket is a variable get from Player class socket = new Socket("severHost", PORT);
        ObjectInputStream messageFromServer = new ObjectInputStream(socket.getInputStream());
        Card card = (Card) messageFromServer.readObject();
        //messageFromServer.close();
        return card;
    }
    public void SendResponseToGameService(Object object) throws IOException, ClassNotFoundException
    {
        //socket is a variable get from Player class socket = new Socket("severHost", PORT);
        ObjectOutputStream messageToServer = new ObjectOutputStream(socket.getOutputStream());
        messageToServer.writeObject(object);
        //messageToServer.close();

    }
    public Object receiveMessage() throws IOException, ClassNotFoundException
    {
        //socket is a variable get from Player class socket = new Socket("severHost", PORT);
        ObjectInputStream messageFromServer = new ObjectInputStream(socket.getInputStream());
        //messageFromServer.close();
        return messageFromServer.readObject();
    }

    public String vCheckInstance (Card card){
        String sCard ="";
        if (card instanceof Clubs) {
            sCard =((Clubs) card).getRate() + " " + ((Clubs) card).getSUIT();
        } else if (card instanceof Hearts) {
            sCard =((Hearts) card).getRate() + " " + ((Hearts) card).getSUIT();
        } else if (card instanceof Diamonds) {
            sCard =((Diamonds) card).getRate() + " " + ((Diamonds) card).getSUIT();
        } else if (card instanceof Spades) {
            sCard =((Spades) card).getRate() + " " + ((Spades) card).getSUIT();
        }
        return sCard;
    }

    public void makeStake(int rate){
        myBid.raisedPlayer = false;
        myBid.setRate(rate);
        myBid.setAccountPlayer(account);
        if(rate>maxBid)myBid.raisedPlayer = true;//To do: make setter
        Game.vSetBank(bankScore+rate);
        //System.out.println(name+" делает ставку "+myBid.getRate());
        Game.chat.setText(name+" делает ставку "+myBid.getRate());
        try {
            SendResponseToGameService(myBid);
            Game.vSetAccount(account);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    

}