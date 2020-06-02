package GameService;



import java.io.Serializable;

public class GameInfo implements Serializable {
    public String name;
    public String status;
    public int score;
    public int maxBid = 0;
    public int callBid = 0;
    public int bid = 0;
    public int bankScore = 0;
}
