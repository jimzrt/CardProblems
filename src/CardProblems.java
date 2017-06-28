import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;


public class CardProblems {


    public static void main(String[] args) {
        Set<String> suits = set("s", "h", "d", "c");
        Set<String> ranks = set("A", "2", "3", "4", "5", "6", "7", "8", "9", "T", "J", "Q", "K");
        Set<String> deck = cross(suits, ranks);
        System.out.println("Es sind " + deck.size() + " Karten im Deck.");
        Set<String> possibleHands = possibleHands(deck, 3);

        System.out.println("Es gibt " + possibleHands.size() + " mögliche Kombinationen von Karten, wenn drei Karten gezogen werden.");
        System.out.println(possibleHandsCount(deck,3,null));
        System.out.println("\nAnna zieht drei Karten.");
        Set<String> sameSuitHands = filterCards(possibleHands, sameSuit());
        System.out.println(sameSuitHands.size());
        System.out.println("Die Wahrscheinlichkeit, dass Anna drei Karten mit der selben Farbe gezogen hat, beträtgt " + getProbability(sameSuitHands, possibleHands) + "%.");
        Set<String> sameRankHands = filterCards(possibleHands, sameRank());
        System.out.println("Möglichkeit A: Anna legt die drei Karten zurück und Ben zieht drei Karten.");
        System.out.println("Die Wahrscheinlichkeit, dass Ben drei Karten mit dem selben Wert gezogen hat, beträtgt " + getProbability(sameRankHands, possibleHands) + "%.");

        System.out.println("\nMöglichkeit B: Anna legt die drei Karten nicht zurück und Ben zieht drei Karten.");

        System.out.println(sameSuit().test("sAh2"));
        System.out.println(sameSuit().test("sAs3"));

        double sumOfProbability = 0.0;
        Set<String> tempDeck;
        Set<String> tempPossibleHands;
        int counter = 0;
        for (String cards : possibleHands) {
            tempDeck = removeCardsFromDeck(cards, deck);
            tempPossibleHands = possibleHands(tempDeck, 3);
            sameRankHands = filterCards(tempPossibleHands, sameRank());
            sumOfProbability += getProbability(sameRankHands, tempPossibleHands);
        //    System.out.println(++counter);
        }
        System.out.println("Die Wahrscheinlichkeit, dass Ben drei Karten mit dem selben Wert gezogen hat, beträgt " + sumOfProbability / possibleHands.size() + "%");
    }

    private static Set<String> removeCardsFromDeck(String cards, Set<String> deck) {
        Set<String> tempDeck = new HashSet<>(deck);
        for (String card : getCards(cards)) {
            tempDeck.remove(card);
        }
        return tempDeck;
    }

    public static Set<String> set(String... items) {
        return new HashSet<>(Arrays.asList(items));
    }

    public static Set<String> cross(Set<String> a, Set<String> b) {
        Set<String> cross = new HashSet<>();

        for (String aItem : a) {
            for (String bItem : b) {
                cross.add(aItem + bItem);
            }
        }

        return cross;
    }


    private static Set<String> crossNoRepetition(Set<String> hand, Set<String> deck) {
        Set<String> cross = new HashSet<>();

        for (String handCard : hand) {
            for (String deckCard : deck) {
                if (!getCards(handCard).contains(deckCard))
                    cross.add(handCard + deckCard);
            }
        }

        return cross;
    }

    private static Set<String> possibleHands(Set<String> deck, int cardCount) {
        Set<String> hand = new HashSet<>(deck);

        for (int i = 1; i < cardCount; i++) {
            hand = crossNoRepetition(hand, deck);
        }

        return hand;
    }

    private static int possibleHandsCount(Set<String> deck, int cardCount, Predicate<String> predicate){
        int count = deck.size();
        for (int i = 1; i < cardCount; i++) {
            count += (deck.size() * (count-(i+1)));
        }
        return count;
    }

    private static int crossNoRepetitionCount(int count, Set<String> deck, Predicate<String> predicate) {

            count += (deck.size() * count);


        return count;
    }


    private static List<String> getCards(String handCard) {
        List<String> cards = new ArrayList<>();

        for (int i = 0; i < handCard.length(); i = i + 2) {
            cards.add(handCard.substring(i, i + 2));
        }

        return cards;
    }

    private static String getSuit(String p) {
        return p.substring(0, 1);
    }

    private static String getRank(String p) {
        return p.substring(1, 2);
    }


    public static Set<String> filterCards(Set<String> cards, Predicate<String> predicate) {
        return cards.stream().filter(predicate).collect(Collectors.toSet());
    }


    public static Predicate<String> sameSuit() {
        return (String cards) -> {

            String suit = getSuit(cards);
            for (String card : getCards(cards)) {
                if (!getSuit(card).equals(suit))
                    return false;
            }
            return true;

        };
    }

    public static Predicate<String> sameRank() {
        return (String cards) -> {

            String rank = getRank(cards);
            for (String card : getCards(cards)) {
                if (!getRank(card).equals(rank))
                    return false;
            }
            return true;

        };
    }


    private static double getProbability(Set<String> eventSpace, Set<String> sampleSpace) {
        return ((double) eventSpace.size() / sampleSpace.size()) * 100;
    }


}