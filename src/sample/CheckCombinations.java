package sample;

import java.io.Serializable;
import java.util.*;


public class CheckCombinations implements Serializable {
    public ArrayList<Card> hand;
    public ArrayList<Card> cardsOnTable;

    public boolean kicker =false;
    public boolean pair = false;
    public boolean set = false;
    public boolean quads = false;
    public boolean flush = false;
    public ArrayList <Card> combination;

    public void checkSeq(ArrayList<Card> cards) {
        //cards.forEach(card -> System.out.println(card.rate));
        combination = new ArrayList<>();
        cards.sort(Comparator.comparing(o -> Integer.parseInt(o.rate)));
        Map<Card, Integer> cardMap = new HashMap<Card, Integer>();
        int countRate = 0;
        int countSameSuit = 0;
        for (Card card : cards) {
            countRate = (int) cards.stream().filter(card1 -> card1.rate.equals(card.rate)).count();
            if (countRate > 1) cardMap.put(card, countRate);
            countSameSuit = (int) cards.stream().filter(card1 -> card1.SUIT.equals(card.SUIT)).count();
            if (countSameSuit > 4) {
                flush = true;
                combination.add(card);
                //System.out.println("FLUSH OF ");
            }
        }
        //если не flush
        if (!flush) {
            cardMap.forEach((card, integer) -> {
                if (integer > 3) {
                    quads = true;
                    combination.add(card);
                    System.out.println("FOUR OF KIND " + card.rate + " " + card.SUIT);
                } else if (integer > 2 && !quads) {
                    set = true;
                    combination.add(card);
                    System.out.println("SET OF " + card.rate + " " + card.SUIT);
                } else if (integer > 1) {
                    pair = true;
                    combination.add(card);
                    System.out.println("PAIR OF " + card.rate + " " + card.SUIT);
                }
            });
            System.out.println("----------------------------------------------------------------------------");
            if (quads) {
                System.out.println("FOUR OF KIND ");
            } else if (set && pair) {
                System.out.println("FULL HOUSE COMBINATION :");
            } else if (pair) {
                if (combination.size() > 2) {
                    System.out.println("TWO PAIRS COMBINATION :");
                } else System.out.println("PAIR COMBINATION :");

            } else if (countRate == 1) {
                kicker = true;
                combination.add(cards.get(cards.size() - 1));
                System.out.println("JUST KICKER :");
            }
        }
        else if(flush){
            combination.sort(Comparator.comparing(o -> Integer.parseInt(o.rate)));
            int iCount = 1;
            for(int i=0; i<cards.size()-1; i++) {
                if (Integer.parseInt(cards.get(i + 1).rate) - Integer.parseInt(cards.get(i).rate) == 1) {
                    iCount++;
                }
            }
            //System.out.println(iCount);
            if(iCount>4&&combination.size()>5){
                while (combination.size()>5)combination.remove(0);
                if(combination.get(combination.size()-1).rate.equals("14"))System.out.println("ROYAL FLUSH OF:");
            }
            else if(iCount>4){
                System.out.println("STRAIGHT FLUSH OF :");
            }
            else System.out.println("FLUSH OF :");
        }
        combination.forEach(card -> System.out.println(card.rate + " " + card.SUIT));
    }

}
