package sample;

import Deck.Card;

import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;

public abstract class GameRound implements Serializable {
    public abstract void vDealCardsToPlayers();
}
