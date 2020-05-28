package Player;

import java.io.Serializable;

public class RoundStakes implements Serializable {
    private int rate = 0;
    private String playerName = "";
    private int previousBid = 0;
    private int accountPlayer;
    public int bank = 0;
    public boolean raisedPlayer = false;

    public RoundStakes(int rate, String playerName){
        this.rate = rate;
        this.playerName = playerName;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate){
        this.rate = rate;
    }

    public int getAccountPlayer(){
        return accountPlayer;
    }

    public void setAccountPlayer(int account){
        this.accountPlayer = account;
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
