package sample;

import com.sun.scenario.effect.impl.sw.java.JSWBlend_SRC_OUTPeer;
import org.w3c.dom.ls.LSOutput;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class CheckCombinations implements Serializable {
    public ArrayList<Card> hand;
    public ArrayList<Card> cardsOnTable;
    public ArrayList<Card> checkCards;

    public boolean quads = false;
    public boolean flush = false;
    public boolean straight = false;
    public boolean straightFlush = false;
    public boolean flushSpades = false;
    public boolean flushHearts = false;
    public boolean flushDiamonds = false;
    public boolean flushClubs = false;
    public ArrayList <Card> diamonds;
    public ArrayList <Card> spades;
    public ArrayList <Card> clubs;
    public ArrayList <Card> hearts;
    public boolean checkFlush(){
        checkCards.addAll(hand);
        checkCards.addAll(cardsOnTable);
        int d = 0;
        int h = 0;
        int s = 0;
        int c = 0;

        for(Card card: checkCards){
            if(card.SUIT=="Spades"){
                spades.add(card);
            }
            else if(card.SUIT=="Hearts"){
                hearts.add(card);
            }
            else if(card.SUIT=="Clubs"){
                clubs.add(card);
            }
            else if(card.SUIT=="Diamonds"){
                diamonds.add(card);
            }
        }
        if(spades.size()>4)flushSpades=true;
        if(hearts.size()>4)flushHearts=true;
        if(clubs.size()>4)flushClubs=true;
        if(diamonds.size()>4)flushDiamonds=true;

        if(flushSpades||flushHearts||flushClubs||flushDiamonds)flush=true;
        return flush;
    }

    public boolean checkStraightFlush (ArrayList<Card> cards){
        cards.sort((o1, o2) -> o1.SUIT.compareTo(o2.SUIT));
        return straightFlush;
    }

    public void checkSequence(ArrayList<Card> cards){
        cards.sort(Comparator.comparing(o->Integer.parseInt(o.rate)));
        cards.forEach(card -> System.out.println(card.rate+" "+card.SUIT));
        int iCount = 1;
        int iSameRates = 1;
       for(int i=0; i<cards.size()-1; i++){
           if(Integer.parseInt(cards.get(i+1).rate)-Integer.parseInt(cards.get(i).rate)==1){
               iCount++;
           }
           //else if(Integer.parseInt(cards.get(i+1).rate)==(Integer.parseInt(cards.get(i).rate))){
           //    iSameRates++;
           //
           //}
       }
        System.out.println(iCount+ " "+ iSameRates);
       //если у обоих игроков стрит, флеш стрит, то нужно проверить крайнюю карту в последовательности на rate
       if(iCount>4&&flush){
           straightFlush = true;
           System.out.println("straightFlush");
       }
       else if(iCount>4){
           straight = true;
           System.out.println("straight");
       }
       else if(iCount<5){
           for(Card card : cards){
               for(int i=1; i<cards.size();i++){
                   if(card.rate.equals(cards.get(i).rate)){
                       iSameRates++;
                   }
               }
           }
           if(iSameRates>3){
               quads=true;
               System.out.println("quads");
           }
       }


    }


}
