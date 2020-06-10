package GameService;

import java.io.Serializable;

public class StakeInfo implements Serializable {
    private int stake = 0;
    private int id = 0;
    private int score = 0;
    public StakeInfo(int stake, int id, int score){
        this.stake = stake;
        this.id = id;
        this.score = score;
    }

    public int getStake() {
        return stake;
    }

    public void setStake(int stake) {
        this.stake = stake;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
