package sample;

import com.sun.management.GarbageCollectionNotificationInfo;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Player extends Thread{
    public static int iHandSize = 2;
    public ArrayList<Card> hand = new ArrayList<>(iHandSize);
    public ArrayList<Card> cardsOnTable = new ArrayList<>(5);
    private String name;
    private int account = 10000;
    volatile public static int acc = 10000;
    //To do: make enum
    private final String requestBid = "your bid mr.";
    private final String requestFlop = "open flop";
    private final String requestTurn = "open turn";
    private final String requestRiver = "open river";
    String filePathCard = "D:/firstUI/src/assets/Deck/";
    Socket socket;
    Stakes myBid = new Stakes(0, name);
    public static int maxBid = 0;

    public static int counter = 0;

    Player(String name) throws IOException
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
        } catch (IOException e) {
            e.printStackTrace();
        }
        //System.out.println(socket+" "+ this.name);
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
            if(receivedMessage!=null && receivedMessage instanceof String && receivedMessage.equals(requestFlop)) {
                //System.out.println(name +" "+ receivedMessage);
                try {
                    Card card = receiveCard();
                    File fileCard = new File(filePathCard + card.SUIT + "/" + card.rate + ".jpg");
                    cardsOnTable.add(card);
                    Game.chat.appendText("карта на flop : " + vCheckInstance(card)+"\n");
                    System.out.println(name + " увидел карту на флопе : " + vCheckInstance(card) + ", карт на столе " + cardsOnTable.size());
                    if (cardsOnTable.size() == 1) Game.f1.setImage(new Image(fileCard.toURI().toString()));
                    if (cardsOnTable.size() == 2) Game.f2.setImage(new Image(fileCard.toURI().toString()));
                    if (cardsOnTable.size() == 3) Game.f3.setImage(new Image(fileCard.toURI().toString()));
                } catch (IOException | ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
            //Open turn
            if(receivedMessage!=null && receivedMessage instanceof String && receivedMessage.equals(requestTurn)) {
                //System.out.println(name +" "+ receivedMessage);
                try {
                    Card card = receiveCard();
                    File fileCard = new File(filePathCard + card.SUIT + "/" + card.rate + ".jpg");
                    cardsOnTable.add(card);
                    Game.chat.appendText("карта на turn : " + vCheckInstance(card)+"\n");
                    System.out.println(name + " увидел карту на turn : " + vCheckInstance(card) + ", карт на столе " + cardsOnTable.size());
                    if (cardsOnTable.size() == 4) Game.t1.setImage(new Image(fileCard.toURI().toString()));
                } catch (IOException | ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
            //Open river
            if(receivedMessage!=null && receivedMessage instanceof String && receivedMessage.equals(requestRiver)) {
                //System.out.println(name +" "+ receivedMessage);
                try {
                    Card card = receiveCard();
                    File fileCard = new File(filePathCard + card.SUIT + "/" + card.rate + ".jpg");
                    cardsOnTable.add(card);
                    Game.chat.appendText("карта на river : " + vCheckInstance(card)+"\n");
                    System.out.println(name + " увидел карту на river : " + vCheckInstance(card) + ", карт на столе " + cardsOnTable.size());
                    if (cardsOnTable.size() == 5) Game.r1.setImage(new Image(fileCard.toURI().toString()));
                } catch (IOException | ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
            //Request Bid
            if(receivedMessage!=null && receivedMessage instanceof Stakes/* && receivedMessage.equals(requestBid)*/){
                //System.out.println(name +" "+ receivedMessage);
                //System.out.println(requestBid+" "+name);
                Stakes stakes = (Stakes)receivedMessage;
                //maxBid = ((Stakes) receivedMessage).getRate();
                //Game.vSetBank(stakes.bank);
                maxBid = stakes.getRate();
                //Game.stakeSpin.setMin(maxBid);
                Game.chat.appendText(requestBid+" "+name+" ?"+"\n");
                while(true){
                    System.out.println(Game.bReadyStake);
                    if(Game.bReadyStake&&Game.iStake>=maxBid) {
                        System.out.println("Ставка сделана");
                        break;
                    }
                }
                Integer bidResponse = Game.iStake;
                account=account-Game.iStake;

                System.out.println(acc+" account status");


                Game.chat.appendText(bidResponse+"\n");
                makeStake(bidResponse);
                Game.bReadyStake = false;

            }

            //}
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
        myBid.setRate(rate);
        myBid.setAccountPlayer(account);
        Game.vSetBank(account);
        //System.out.println(name+" делает ставку "+myBid.getRate());
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