package sample;

public class Clubs extends Card {
    private String rate;
    private final String SUIT = "Clubs";
    public Clubs(String rate){
        this.rate = rate;
        super.rate = rate;
        super.SUIT = SUIT;
    }

    public String getRate() {
        return rate;
    }

    public String getSUIT() {
        return SUIT;
    }
}
