package sample;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;

public class Server extends Thread{
    public static final int PORT = 3443;
    public static LinkedList<Player> playersList = new LinkedList<>(); // список всех игроков
    ServerSocket server;
    ArrayList<Socket> socketList;
    private int iCountPlayers = 1;
    int count = 0;

    @Override
    public void run() {

        try {
            server = new ServerSocket(PORT);
            socketList = new ArrayList<>(iCountPlayers);

        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            for(int i=0; i<socketList.size(); i++){
                // Блокируется до возникновения нового соединения:
                Socket socket = server.accept();
                System.out.println("Подключено "+ ++iCountPlayers);
                socketList.add(socket); // добавить новое соединенние в список
                System.out.println("Ждем остальных игроков...");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                server.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }




}
