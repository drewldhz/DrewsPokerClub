package sample;

import Deck.Card;
import Deck.Deck;

public class Turn extends GameRound{
    public Turn(){
        vDealCardsToPlayers();
    }
    public Card getTurnCard() {
        return turnCard;
    }
    private Card turnCard;
    @Override
    public void vDealCardsToPlayers() {
        turnCard = Deck.extractCard();
    }
}
