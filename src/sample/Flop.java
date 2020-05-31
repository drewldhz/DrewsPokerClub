package sample;

import Deck.Card;
import Deck.Deck;

import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;

public class Flop extends GameRound {
    private final int countCards = 3;
    private ArrayList<Card> cardsFlop;

    public Flop(){
        vDealCardsToPlayers();
    }
    public ArrayList<Card> getCardsFlop() {
        return cardsFlop;
    }

    @Override
    public void vDealCardsToPlayers() {
        cardsFlop = new ArrayList<Card>(3);
        while(cardsFlop.size()<countCards)cardsFlop.add(Deck.extractCard());
    }
}
