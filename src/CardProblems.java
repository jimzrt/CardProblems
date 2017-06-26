import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;


public class CardProblems {


    public static void main(String[] args) {
        Set<String> suits = set("s", "h", "d", "c");
        Set<String> ranks = set("A", "2", "3", "4", "5", "6", "7", "8", "9", "T", "J", "Q", "K");
        Set<String> deck = cross(suits, ranks);
        System.out.println("Es gibt " + deck.size() + " Karten im Deck.");
        Set<String> possibleHands = possibleHands(deck, 4);
        System.out.println("Es gibt " + possibleHands.size() + " mögliche Kombinationen von Karten, wenn drei Karten gezogen werden.");

        Set<String> sameSuitHands = filterCards(possibleHands, sameSuit());
        System.out.println("Die Wahrscheinlichkeit, dass Anna drei Karten mit der selben Farbe gezogen hat, beträtgt " + getProbability(sameSuitHands, possibleHands) + "%.");
        Set<String> sameRankHands = filterCards(possibleHands, sameRank());
        System.out.println("Die Wahrscheinlichkeit, dass Ben drei Karten mit dem selben Wert gezogen hat, beträtgt " + getProbability(sameRankHands, possibleHands) + "%.");
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