package GameService;

import Player.Player;

import java.io.Serializable;

public class PlayerID implements Serializable {
    private int playersID = 0;

    public PlayerID(int id){
        this.playersID = id;
    }
    public int getPlayersID() {
        return playersID;
    }

    public void setPlayersID(int playersID) {
        this.playersID = playersID;
    }
}
