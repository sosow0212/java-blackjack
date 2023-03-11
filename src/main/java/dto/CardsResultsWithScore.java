package dto;

import domain.card.Card;
import domain.participants.Participant;
import java.util.ArrayList;
import java.util.List;

public class CardsResultsWithScore {

    private final String name;
    private final List<String> drawnCards;
    private final int score;

    private CardsResultsWithScore(final String name, final List<String> drawnCards, final int score) {
        this.name = name;
        this.drawnCards = drawnCards;
        this.score = score;
    }

    public static CardsResultsWithScore toDto(final Participant participant) {
        List<String> cardInfos = new ArrayList<>();
        List<Card> cards = participant.getDrawnCards();
        for (Card drawnCard : cards) {
            cardInfos.add(drawnCard.getValue().getName() + drawnCard.getType().getName());
        }

        return new CardsResultsWithScore(participant.getName(), cardInfos, participant.calculateCardScore());
    }

    public String getName() {
        return name;
    }

    public List<String> getDrawnCards() {
        return drawnCards;
    }

    public int getScore() {
        return score;
    }
}
