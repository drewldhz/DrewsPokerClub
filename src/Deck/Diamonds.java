package Deck;

public class Diamonds extends Card {
    private String rate;
    private final String SUIT = "Diamonds";
    public Diamonds(String rate){
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
