package sample;

import java.io.Serializable;

public class Stakes implements Serializable {
    private int rate = 0;
    private String playerName = "";
    private int previousBid = 0;

    public Stakes(int rate, String playerName){
        this.rate = rate;
        this.playerName = playerName;
    }

    public int getRate() {
        return rate;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getPreviousBid() {
        return previousBid;
    }

    public void setPreviousBid(int previousBid) {
        this.previousBid = previousBid;
    }
}
