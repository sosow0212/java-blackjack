package domain;

import java.util.Collections;
import java.util.List;

public class Player extends Participant {

    private static final double WINNING_RATIO = 1.5;
    private static final int BUST_NUMBER = 21;

    public Player(final Status status, final DrawnCards drawnCards) {
        super(status, drawnCards);
    }

    @Override
    public List<Card> openDrawnCards() {
        return Collections.unmodifiableList(drawnCards.getCards());
    }

    @Override
    public boolean canDrawMore() {
        return calculateCardScore() < BUST_NUMBER;
    }

    public int bustAccount() {
        int account = status.getAccount();
        status.bustAccount(account);
        return account;
    }

    public void winGame() {
        int winningPrice = (int) (status.getAccount() * (WINNING_RATIO - 1));
        status.winGame(winningPrice);
    }

    public boolean isScoreHighThanDealer(final int dealerScore) {
        return calculateCardScore() > dealerScore;
    }
}
