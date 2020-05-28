package GameService;

import Deck.Card;
import Deck.Deck;
import Player.CheckCombinations;
import Player.RoundStakes;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;

public class HoldemPoker {
    private final int handSize = 2;
    public ArrayList<Card> cardsOnTable = new ArrayList<>(5);
    public ArrayList<Socket> fauldPlayers = new ArrayList<>();
    public boolean gameState = true;//false-"over"
    public boolean cardsOnHand = false;
    public boolean flopOpened = false;
    public boolean turnOpened = false;
    public boolean riverOpened = false;
    public boolean bidding = false;
    public static boolean biddingEnd = false;
    private int bank = 0;
    public static int maxBid = 0;
    public Socket raisedPlayerSocket = null;
    public RoundStakes stake;
    public boolean roundEnd = false;
    public HoldemPoker(LinkedList<Socket> playerHands) throws IOException, ClassNotFoundException {
        String actualMessage = "";
        while (gameState){
            if(!cardsOnHand){
                vDealTheCardsToPlayers(playerHands,handSize);
                //Game.chat.setText("Раздача карт прошла...торги на префлопе");
                actualMessage = "Раздача карт прошла...торги на префлопе";
                System.out.println(actualMessage);
            }
            //инициировать торги - bidding
            if(!bidding)vRequestBidding(playerHands);
           if(!flopOpened && bidding){
                //Game.chat.setText("Раздача на Flop");

                vDealTheCardsToTable(playerHands,3);
                flopOpened = true;
                actualMessage = "Раздача на flop прошла...торги на flop";
                System.out.println(actualMessage);
               //Game.chat.setText("Раздача на flop прошла...торги на flop");
                bidding = false;
                biddingEnd = false;
            }
           //Open turn
            if(!turnOpened && flopOpened && bidding){
                //Game.chat.setText("Раздача на Turn");
                System.out.println("Раздача на Turn");
                vDealTheCardsToTable(playerHands,1);
                turnOpened = true;
                actualMessage = "Раздача на turn прошла...торги на turn";
                System.out.println(actualMessage);
                bidding = false;
                biddingEnd = false;
            }
            //Open river
            if(!riverOpened && turnOpened && bidding){
                System.out.println("Раздача на River");
                vDealTheCardsToTable(playerHands,1);
                riverOpened = true;
                actualMessage = "Раздача на river прошла...торги на river";
                System.out.println(actualMessage);
                bidding = false;
                biddingEnd = false;
            }
        }
    }

    public void vDealTheCardsToPlayers(LinkedList<Socket> playerHands, int handSize) throws IOException {
        System.out.println("Функция раздачи карт :");
        int iCountCards = 0;
        while (iCountCards<handSize) {
            playerHands.forEach(player-> {
                    try {
                        sendMessage(player, "send cards");
                        sendMessage(player, Deck.extractCard());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            iCountCards++;
        }
        cardsOnHand = true;
        try {
            Thread.currentThread().sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void vDealTheCardsToTable(LinkedList<Socket> playerTable, int quantityCards) throws IOException {
        int iCountCards = 0;
        while (iCountCards<quantityCards) {
            if(quantityCards==3&&!flopOpened){
                System.out.println("Раздача на Flop");
                Card card = Deck.extractCard();
                playerTable.forEach(player-> {
                    try {
                        sendMessage(player, "open flop");
                        sendMessage(player,card);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
                iCountCards++;
            }
            if(quantityCards==1 && !turnOpened && flopOpened){
                Card card = Deck.extractCard();
                playerTable.forEach(player-> {
                    try {
                        sendMessage(player, "open turn");
                        sendMessage(player,card);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
                iCountCards++;
            }
            if(quantityCards==1 && !riverOpened && turnOpened){
                Card card = Deck.extractCard();
                playerTable.forEach(player-> {
                    try {
                        sendMessage(player, "open river");
                        sendMessage(player,card);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
                iCountCards++;
            }
            bidding = false;
            try {
                Thread.currentThread().sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendMessage(Socket socket, Object message) throws IOException
    {
            ObjectOutputStream testToClient = new ObjectOutputStream(socket.getOutputStream());
            testToClient.writeObject(message);
            testToClient.flush();
    }

    public Object receiveMessage(Socket player) throws IOException, ClassNotFoundException
    {
        //socket is a variable get from Player class socket = new Socket("severHost", PORT);
        ObjectInputStream messageFromServer = new ObjectInputStream(player.getInputStream());
        Object object = messageFromServer.readObject();
        return object;
    }

    public void vRequestBidding(LinkedList<Socket> players) throws IOException, ClassNotFoundException {
        System.out.println("Функция запроса ставок :");
        RoundStakes myBid = null;
        biddingEnd = false;
        while (!biddingEnd){
            for(Socket player: players){
                if(!fauldPlayers.isEmpty() && fauldPlayers.contains(player)) continue;
                if(player==raisedPlayerSocket){
                    Game.chat.setText("необходимо заканчивать торги");
                    biddingEnd = true;
                    bidding = true;
                    prepareNewRoundOfBidding(players);
                    break;
                }
                //
                stake = new RoundStakes(maxBid,"");
                stake.bank = bank;
                sendMessage(player, stake);
                myBid = (RoundStakes) receiveMessage(player);
                if(myBid.raisedPlayer)raisedPlayerSocket = player;
                if(myBid.getRate()>maxBid)maxBid=myBid.getRate();
                else if(myBid.getRate()<0){
                    fauldPlayers.add(player);
                    System.out.println("fauld");
                    continue;
                }
                else if(myBid.getRate()==maxBid)Game.chat.setText(myBid.getPlayerName() + "уравнивает ставку"+"\n");
                bank+=myBid.getRate();
            }
            if(maxBid==0){
                biddingEnd = true;
                bidding = true;
                prepareNewRoundOfBidding(players);
                break;
            }
            //if(players.size()-fauldPlayers.size()==1||players.size()-fauldPlayers.size()==0){
            // //объявить начало новой игры
            //}
        }
        if(riverOpened){
            roundEnd=true;
            for(Socket player: players){
                sendMessage(player, new CheckCombinations());
            }
        }
    }

    public int vBidding(RoundStakes myBid, int max){
        boolean nextRound = false;
        int maxBid = max;
        String wordFromPlayer = "";
        if(myBid.getRate()>maxBid){
            maxBid = myBid.getRate();
            wordFromPlayer = " повышает ставку ";
            System.out.println(myBid.getPlayerName()+wordFromPlayer+" : ставка "+myBid.getRate());
        }
        else if(myBid.getRate()==maxBid){
            wordFromPlayer = " уравнивает ставку ";
            System.out.println(myBid.getPlayerName()+wordFromPlayer+" : ставка "+myBid.getRate());
        }
        else if(myBid.getRate()==0){
            wordFromPlayer = " пропускает ход торгов ";
            System.out.println(myBid.getPlayerName()+wordFromPlayer+" : ставка "+myBid.getRate());
        }
        else if(myBid.getRate()<0){
            wordFromPlayer = " сбрасывает карты ";
            System.out.println(myBid.getPlayerName()+wordFromPlayer+" : ставка "+myBid.getRate());
        }
        return maxBid;
    }

    void prepareNewRoundOfBidding(LinkedList<Socket> players){
        Game.chat.setText("подготовка к раздаче карт");
        biddingEnd = true;
        raisedPlayerSocket = null;
        maxBid = 0;
        bank = 0;
        fauldPlayers.clear();
        GameInfo gameInfo = new GameInfo();
        for(Socket player: players){
            try {
                sendMessage(player,gameInfo);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
