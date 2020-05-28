package Player;

import Deck.Card;
import GameService.Game;


import java.io.Serializable;
import java.util.*;


public class CheckCombinations implements Serializable {
    private boolean kicker =false;
    private boolean pair = false;
    private boolean set = false;
    private boolean quads = false;
    private boolean fullHouse = false;
    private boolean flush = false;
    private ArrayList <Card> combination;
    private int combinationPower = 0;
    private Map<String, Integer> mapCombinationsPower;
    //checkSeq должен возвращать силу собранной комбинации в int
    public CheckCombinations(){
        initializeCombinationsPower();
    }
    public void initializeCombinationsPower(){
        mapCombinationsPower = new HashMap<>();
        mapCombinationsPower.put("KICKER", 1);
        mapCombinationsPower.put("PAIR",2);
        mapCombinationsPower.put("TWO PAIRS",3);
        mapCombinationsPower.put("SET",4);
        mapCombinationsPower.put("STRAIGHT",5);
        mapCombinationsPower.put("FLUSH",6);
        mapCombinationsPower.put("FULL HOUSE", 7);
        mapCombinationsPower.put("FOUR OF KIND", 8);
        mapCombinationsPower.put("STRAIGHT FLUSH",9);
        mapCombinationsPower.put("ROYAL FLUSH",10);
    }
    //Добавить проверку на STRAIGHT
    public int checkSeq(ArrayList<Card> cards) {
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
                    combinationPower = mapCombinationsPower.get("SET");
                } else if (integer > 1) {
                    pair = true;
                    combination.add(card);
                    System.out.println("PAIR OF " + card.rate + " " + card.SUIT);
                }
            });
            System.out.println("----------------------------------------------------------------------------");
            if (quads) {
                System.out.println("FOUR OF KIND ");
                combinationPower = mapCombinationsPower.get("FOUR OF KIND");
            } else if (set && pair) {
                System.out.println("FULL HOUSE COMBINATION :");
                combinationPower = mapCombinationsPower.get("FULL HOUSE");
            } else if (pair) {
                if (combination.size() > 2) {
                    combinationPower = mapCombinationsPower.get("TWO PAIRS");
                    System.out.println("TWO PAIRS COMBINATION :");
                } else{
                    combinationPower = mapCombinationsPower.get("PAIR");
                    System.out.println("PAIR COMBINATION :");
                }

            } else if (countRate == 1) {
                kicker = true;
                combination.add(cards.get(cards.size() - 1));
                combinationPower = mapCombinationsPower.get("KICKER");
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
            if(iCount>4&&combination.size()>5){
                while (combination.size()>5)combination.remove(0);
                if(combination.get(combination.size()-1).rate.equals("14")){
                    combinationPower = mapCombinationsPower.get("ROYAL FLUSH");
                    System.out.println("ROYAL FLUSH OF:");
                }
            }
            else if(iCount>4){
                System.out.println("STRAIGHT FLUSH OF :");
                combinationPower = mapCombinationsPower.get("STRAIGHT FLUSH");
            }
            else {
                System.out.println("FLUSH OF :");
                combinationPower = mapCombinationsPower.get("FLUSH");
            }
        }
        combination.forEach(card -> System.out.println(card.rate + " " + card.SUIT));
        Game.vAnimateValidation(combination);
        return combinationPower;
    }


}
