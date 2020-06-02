package GameService;

import Deck.Card;
import Player.Player;
import javafx.application.Platform;
import sample.PlayerUI;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class GameService extends  Thread{
    private static final int PORT = 10000;
    ServerSocket serverSocket;
    InetAddress address;
    public static LinkedList<Player> playersList = new LinkedList<>(); // список всех игроков
    Socket playerSocket;
    public static int counter = 0;
    private int iCountPlayers = 2;
    public LinkedList<Socket> socketList;
    public static Map<Socket, String> playersMap = new HashMap<>();



    public GameService() {
        start();
    }

    @Override
    public void run() {
        //System.out.println("Размер колоды до раздачи карты :"+Deck.shuffledDeck.size());
            try {
                serverSocket = new ServerSocket(PORT);
                socketList = new LinkedList<Socket>();

                while(socketList.size()<iCountPlayers){
                    playerSocket = serverSocket.accept();
                    socketList.add(playerSocket); // добавить новое соединенние в список
                    String name = (String)(receiveMessage(playerSocket));
                    System.out.println(name);
                    playersMap.put(playerSocket, name);


                    System.out.println("Подключено "+ socketList.size());
                    Game.chat.appendText("Подключено "+ socketList.size()+"\n");
                    Game.chat.appendText("Ждем остальных игроков..."+"\n");

                }
                //System.out.println("Все игроки подключены , количество игроков "+socketList.size());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                socketList.forEach(socket -> {
                    playersMap.forEach((socket1, s) -> {
                        try {
                            if(!socket.equals(socket1)){
                                SignalToCreatePlayer signalToCreatePlayer = new SignalToCreatePlayer();
                                signalToCreatePlayer.setName(s);
                                sendMessage(socket,signalToCreatePlayer);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                });
                Game.chat.appendText("Все игроки подключены , количество игроков "+socketList.size()+"\n");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //System.out.println("Понеслась");
                Game.chat.appendText("Round "+" started+\n");


               try {
                    new HoldemPoker(socketList);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }


            } catch (IOException | ClassNotFoundException ex) {
                ex.printStackTrace();
            } finally {
            try {
                serverSocket.close();
            } catch (IOException e) {
            e.printStackTrace();
        }
    }

    }

    public void sendMessage(Socket socket, Object object) throws IOException {
        ObjectOutputStream messageToPlayers = new ObjectOutputStream(socket.getOutputStream());
        messageToPlayers.writeObject(object);
    }


    public Object receiveMessage(Socket player) throws IOException, ClassNotFoundException {
        //socket is a variable get from Player class socket = new Socket("severHost", PORT);
        ObjectInputStream messageFromServer = new ObjectInputStream(player.getInputStream());
        Object object = messageFromServer.readObject();
        return object;
    }


}
