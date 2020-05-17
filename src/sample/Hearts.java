package sample;

public class Hearts extends Card {
    private String rate;
    private final String SUIT = "Hearts";
    public Hearts(String rate){
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
