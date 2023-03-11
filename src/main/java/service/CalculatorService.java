package service;

import domain.card.CardDeck;
import domain.command.Command;
import domain.participants.Dealer;
import domain.participants.Participant;
import domain.participants.Player;
import domain.participants.Players;

public class CalculatorService {

    private static final int CARDS_SPLIT_COUNT_OF_INIT = 2;

    public void splitCards(final Players players, final Dealer dealer, final CardDeck cardDeck) {
        splitCard(dealer, cardDeck);
        players.stream()
                .forEach(player -> splitCard(player, cardDeck));
    }

    private void splitCard(final Participant participant, final CardDeck cardDeck) {
        for (int i = 0; i < CARDS_SPLIT_COUNT_OF_INIT; i++) {
            participant.pickCard(cardDeck.draw());
        }
    }

    public void drawCard(final Player player, final CardDeck cardDeck, final Command command) {
        if (canDrawMore(player, command)) {
            player.pickCard(cardDeck.draw());
        }
    }

    public boolean canDrawMore(final Player player, final Command command) {
        return player.canDrawMore() && command == Command.DRAW;
    }

    public void pickDealerCard(final CardDeck cardDeck, final Dealer dealer) {
        if (dealer.canDrawMore()) {
            dealer.pickCard(cardDeck.draw());
        }
    }

    public boolean canDealerDrawMore(final Dealer dealer) {
        return dealer.canDrawMore();
    }

    public void calculateGameResults(final Players players, final Dealer dealer) {
        players.stream()
                .forEach(player -> calculateAccount(player, dealer));
    }

    private void calculateAccount(final Player player, final Dealer dealer) {
        if (player.isBlackjack() && dealer.isBlackjack()) {
            return;
        }

        if (isAllParticipantBust(player, dealer)) {
            winDealer(player, dealer);
            return;
        }

        if (isPlayerWin(player, dealer)) {
            winPlayer(player, dealer, player.getAccount());
            return;
        }

        winDealer(player, dealer);
    }

    private boolean isPlayerWin(final Player player, final Dealer dealer) {
        return (player.isScoreHighThanDealer(dealer.calculateCardScore()) && !player.isBust())
                || dealer.isBust();
    }

    private void winPlayer(final Player player, final Dealer dealer, final int playerAccount) {
        player.winGame();
        dealer.loseGame(playerAccount);
    }

    private void winDealer(final Player player, final Dealer dealer) {
        dealer.winGame(player.bustAccount());
    }

    private boolean isAllParticipantBust(final Player player, final Dealer dealer) {
        return player.isBust() && dealer.isBust();
    }
}
