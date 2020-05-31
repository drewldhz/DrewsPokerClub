package sample;

import Deck.Card;
import Deck.Deck;

public class River extends GameRound {
    public River(){
        vDealCardsToPlayers();
    }
    public Card getRiverCard() {
        return riverCard;
    }
    private Card riverCard;
    @Override
    public void vDealCardsToPlayers() {
        riverCard = Deck.extractCard();
    }
}
