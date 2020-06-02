package Player;

import Deck.Card;

import java.io.Serializable;
import java.util.ArrayList;

public class PlayerHand implements Serializable {
    private ArrayList<Card>hand;

    public ArrayList<Card> getHand() {
        return hand;
    }

    public PlayerHand(ArrayList<Card> hand){
        this.hand = hand;
    }
}
