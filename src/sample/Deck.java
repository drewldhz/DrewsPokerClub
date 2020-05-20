package sample;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.Arrays;

public class Deck implements Externalizable {
    static ArrayList<String> rates = new ArrayList<>();
    static ArrayList<Card> cards = new ArrayList<>();
    static ArrayList<Card> shuffledDeck = new ArrayList<>();

    //Генерация колоды карт
    public static void generateDeck(){
        rates.addAll(Arrays.asList("2","3","4","5","6","7","8","9","10","11","12","13","14"));//0-12
        for(int i=0; i<rates.size(); i++){
            cards.add(new Clubs(rates.get(i)));
            cards.add(new Hearts(rates.get(i)));
            cards.add(new Diamonds(rates.get(i)));
            cards.add(new Spades(rates.get(i)));
        }
        //System.out.println(cards.size());
    }

    //Излечь из колоды 1 карту
    public static Card extractCard(){
        return shuffledDeck.remove(0);
    }
    //Перемешать карты
    public static void shuffleCards(){
        while (cards.size()>0){
            int index = (int)(Math.random()*cards.size());
            Card card = cards.get(index);
            shuffledDeck.add(card);
            cards.remove(index);
        }


        //System.out.println(cards.size()+" в новой колоде");
        //System.out.println(shuffledDeck.size()+" размер shuffledDeck");
        /*for(Card card: shuffledDeck) {
            if (card instanceof Clubs) {
                System.out.println(((Clubs) card).getRate()+" "+((Clubs) card).getSUIT());
            } else if (card instanceof Hearts) {
                System.out.println(((Hearts) card).getRate()+" "+((Hearts) card).getSUIT());
            } else if (card instanceof Diamonds) {
                System.out.println(((Diamonds) card).getRate()+" "+((Diamonds) card).getSUIT());
            } else if (card instanceof Spades) {
                System.out.println(((Spades) card).getRate()+" "+((Spades) card).getSUIT());
            }
        }*/
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(extractCard());
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {

    }
}
