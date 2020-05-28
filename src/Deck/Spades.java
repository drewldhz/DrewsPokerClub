package Deck;

public class Spades extends Card {
    private String rate;
    private final String SUIT = "Spades";
    public Spades(String rate){
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
