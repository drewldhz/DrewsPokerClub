package GameService;

import java.io.Serializable;

public class SignalToCreatePlayer implements Serializable {
    public String name;

    public void setName(String name) {
        this.name = name;
    }

    public String status;
    public int score;
    public SignalToCreatePlayer(){
    }

}
