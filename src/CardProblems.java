import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;


public class CardProblems {

    public static void main(String[] args) {
        Set<String> suits = set("s", "h", "d", "c");
        Set<String> ranks = set("A", "2", "3", "4", "5", "6", "7", "8", "9", "T", "J", "Q", "K");
        Set<String> deck = cross(suits, ranks);
        System.out.println("Es sind " + deck.size() + " Karten im Deck.");

        int cardCount = 3;
        Set<String> possibleHands = possibleHands(deck, cardCount);
        System.out.println("Es gibt " + possibleHands.size() + " mögliche Kombinationen von Karten auf der Hand, wenn " + cardCount
                + " Karten gezogen werden.");

        System.out.println("\nMöglichkeit A: Anna (oder Ben) zieht " + cardCount + " Karten und legt sie wieder zurück. Anschließend" +
                " zieht die andere Person.");

        Set<String> sameSuitHands = filterCards(possibleHands, sameSuit());
        double sameSuitProbability = getProbability(sameSuitHands.size(), possibleHands.size());

        System.out.println("Die Wahrscheinlichkeit, dass Anna " + cardCount + " Karten mit der selben Farbe gezogen hat, beträtgt "
                + String.format("%.3f", sameSuitProbability) + "%.");
        Set<String> sameRankHands = filterCards(possibleHands, sameRank());
        double sameRankProbability = getProbability(sameRankHands.size(), possibleHands.size());
        System.out.println("Die Wahrscheinlichkeit, dass Ben " + cardCount + " Karten mit dem selben Wert gezogen hat, beträtgt " +
                String.format("%.3f", sameRankProbability) + "%.");

        System.out.println("\nMöglichkeit B: Anna zieht " + cardCount + " Karten und legt sie nicht wieder zurück. Anschließend " +
                "zieht Ben.");

        double sumOfProbability = 0.0;
        double probabilityOneLessSameRank = getProbability(sameRankHands.size() - 1, possibleHands.size() - 1);
        double probabilityOneLessNotSameRank = getProbability(sameRankHands.size(), possibleHands.size() - 1);
        for (String cards : possibleHands) {
            sumOfProbability += sameRankHands.contains(cards) ? probabilityOneLessSameRank :
                    probabilityOneLessNotSameRank;
        }

        System.out.println("Die Wahrscheinlichkeit, dass Anna " + cardCount + " Karten mit der selben Farbe gezogen hat, beträtgt "
                + String.format("%.3f", sameSuitProbability) + "%.");
        System.out.println("Die Wahrscheinlichkeit, dass Ben " + cardCount + " Karten mit dem selben Wert gezogen hat, beträgt " +
                String.format("%.3f", sumOfProbability / possibleHands.size()) + "%");

        System.out.println("\nMöglichkeit C: Ben zieht " + cardCount + " Karten und legt sie nicht wieder zurück. Anschließend zieht" +
                " Anna.");

        sumOfProbability = 0.0;
        double probabilityOneLessSameSuit = getProbability(sameSuitHands.size() - 1, possibleHands.size() - 1);
        double probabilityOneLessNotSameSuit = getProbability(sameSuitHands.size(), possibleHands.size() - 1);
        for (String cards : possibleHands) {
            sumOfProbability += sameSuitHands.contains(cards) ? probabilityOneLessSameSuit : probabilityOneLessNotSameSuit;
        }

        System.out.println("Die Wahrscheinlichkeit, dass Ben " + cardCount + " Karten mit dem selben Wert gezogen hat, beträtgt " +
                String.format("%.3f", sameRankProbability) + "%.");
        System.out.println("Die Wahrscheinlichkeit, dass Anna " + cardCount + " Karten mit der selben Farbe gezogen hat, beträgt " +
                String.format("%.3f", sumOfProbability / possibleHands.size()) + "%");

        System.out.println("\n==> Es macht keinen Unterschied, ob die Karten zurückgelegt werden oder wer anfängt.");
    }


    private static Set<String> set(String... items) {
        return new HashSet<>(Arrays.asList(items));
    }

    private static Set<String> cross(Set<String> a, Set<String> b) {
        Set<String> cross = new HashSet<>();

        for (String aItem : a) {
            for (String bItem : b) {
                cross.add(aItem + bItem);
            }
        }

        return cross;
    }

    private static Set<String> crossNoDuplicates(Set<String> hand, Set<String> deck) {
        Set<String> cross = new HashSet<>();

        for (String handCard : hand) {
            for (String deckCard : deck) {
                if (!handCard.contains(deckCard))
                    cross.add(handCard + deckCard);
            }
        }

        return cross;
    }

    private static Set<String> possibleHands(Set<String> deck, int cardCount) {
        Set<String> hands = new HashSet<>(deck);

        for (int i = 1; i < cardCount; i++) {
            hands = crossNoDuplicates(hands, deck);
        }

        return hands;
    }

    private static List<String> getCards(String handCard) {
        List<String> cards = new ArrayList<>();

        for (int i = 0; i < handCard.length(); i = i + 2) {
            cards.add(handCard.substring(i, i + 2));
        }

        return cards;
    }

    private static String getSuit(String card) {
        return card.substring(0, 1);
    }

    private static String getRank(String card) {
        return card.substring(1, 2);
    }

    private static Set<String> filterCards(Set<String> cards, Predicate<String> predicate) {
        return cards.stream().filter(predicate).collect(Collectors.toSet());
    }

    private static Predicate<String> sameSuit() {
        return (String cards) -> {

            String suit = getSuit(cards);
            for (String card : getCards(cards)) {
                if (!getSuit(card).equals(suit))
                    return false;
            }
            return true;

        };
    }

    private static Predicate<String> sameRank() {
        return (String cards) -> {

            String rank = getRank(cards);
            for (String card : getCards(cards)) {
                if (!getRank(card).equals(rank))
                    return false;
            }
            return true;

        };
    }

    private static double getProbability(int eventSpace, int sampleSpace) {
        return ((double) eventSpace / sampleSpace) * 100;
    }

}