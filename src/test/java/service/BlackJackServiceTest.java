package service;

import static org.assertj.core.api.Assertions.assertThat;

import domain.Card;
import domain.CardDeck;
import domain.CardDeckGenerator;
import domain.Dealer;
import domain.DrawCommand;
import domain.DrawnCards;
import domain.Message;
import domain.Name;
import domain.Player;
import domain.Players;
import domain.Type;
import domain.Value;
import dto.DealerWinLoseResult;
import dto.DrawnCardsInfo;
import dto.ParticipantResult;
import dto.WinLoseResult;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BlackJackServiceTest {

    private final BlackJackService blackJackService = new BlackJackService();

    @Test
    @DisplayName("딜러와 플레이어들에게 카드를 나눠준다.")
    void split_cards_to_dealer_and_players() {
        // given
        DrawnCards emptyCards = new DrawnCards(new ArrayList<>());
        CardDeck cardDeck = CardDeckGenerator.create();

        Player player = new Player(new Name("pobi"), emptyCards);
        Players players = new Players(List.of(player));
        Dealer dealer = new Dealer(emptyCards);

        // when
        List<DrawnCardsInfo> result = blackJackService.splitCards(dealer, players, cardDeck);

        // then
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0).getName()).isEqualTo(Message.DEALER_NAME.getMessage());
        assertThat(result.get(1).getName()).isEqualTo(player.getName());
    }

    @Test
    @DisplayName("카드를 뽑아준다. (Command == y)")
    void draw_card_when_command_mean_draw() {
        // given
        DrawnCards emptyCards = new DrawnCards(new ArrayList<>());
        CardDeck cardDeck = CardDeckGenerator.create();

        Player player = new Player(new Name("pobi"), emptyCards);

        DrawCommand drawCommand = new DrawCommand("y");

        // when
        DrawnCardsInfo result = blackJackService.drawCards(cardDeck, player, drawCommand);

        // then
        assertThat(player.openDrawnCards().size()).isEqualTo(1);
        assertThat(result.getDrawnCards().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("카드를 뽑지 않는다. (Command == n)")
    void draw_card_when_command_mean_stop() {
        // given
        DrawnCards emptyCards = new DrawnCards(new ArrayList<>());
        CardDeck cardDeck = CardDeckGenerator.create();

        Player player = new Player(new Name("pobi"), emptyCards);

        DrawCommand drawCommand = new DrawCommand("n");

        // when
        DrawnCardsInfo result = blackJackService.drawCards(cardDeck, player, drawCommand);

        // then
        assertThat(player.openDrawnCards().size()).isEqualTo(0);
        assertThat(result.getDrawnCards().size()).isEqualTo(0);
    }

    @Test
    @DisplayName("플레이어가 뽑은 수의 총합이 버스트 넘버 초과하면 false를 반환한다.")
    void returns_false_when_player_cards_numbers_sum_over_burst() {
        // given
        Card card1 = new Card(Type.CLUB, Value.EIGHT);
        Card card2 = new Card(Type.SPADE, Value.EIGHT);
        Card card3 = new Card(Type.DIAMOND, Value.EIGHT);

        DrawnCards overBurstNumberCards = new DrawnCards(List.of(card1, card2, card3));

        Player player = new Player(new Name("pobi"), overBurstNumberCards);
        DrawCommand drawCommand = new DrawCommand(Message.DRAW_COMMAND.getMessage());

        // when
        boolean result = blackJackService.canDrawMore(player, drawCommand);

        // then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("플레이어가 카드를 더 뽑겠냐는 질문에 stop을 원하면 false를 반환한다.")
    void returns_false_when_player_do_not_want_more_card() {
        // given
        Card card1 = new Card(Type.CLUB, Value.EIGHT);
        Card card2 = new Card(Type.SPADE, Value.EIGHT);

        DrawnCards cards = new DrawnCards(List.of(card1, card2));

        Player player = new Player(new Name("pobi"), cards);
        DrawCommand drawCommand = new DrawCommand(Message.STOP_COMMAND.getMessage());

        // when
        boolean result = blackJackService.canDrawMore(player, drawCommand);

        // then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("플레이어가 카드를 더 뽑고 싶어하고, 플레이어의 카드 총 합이 버스트 넘버를 초과하지 않는다면 true를 반환한다.")
    void returns_true_when_player_want_more_card_and_not_burst() {
        // given
        Card card1 = new Card(Type.CLUB, Value.EIGHT);
        Card card2 = new Card(Type.SPADE, Value.EIGHT);

        DrawnCards cards = new DrawnCards(List.of(card1, card2));

        Player player = new Player(new Name("pobi"), cards);
        DrawCommand drawCommand = new DrawCommand(Message.DRAW_COMMAND.getMessage());

        // when
        boolean result = blackJackService.canDrawMore(player, drawCommand);

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("딜러의 카드 총합이 16이하라면 카드를 뽑는다.")
    void dealer_draws_card_when_cards_sum_under_boundary() {
        // given
        CardDeck cardDeck = CardDeckGenerator.create();

        Card card1 = new Card(Type.CLUB, Value.EIGHT);
        Card card2 = new Card(Type.SPADE, Value.EIGHT);
        List<Card> givenCards = List.of(card1, card2);
        int sumBeforeDrawn = card1.getScore() + card2.getScore();

        DrawnCards drawnCards = new DrawnCards(givenCards);

        Dealer dealer = new Dealer(drawnCards);

        // when
        blackJackService.pickDealerCard(cardDeck, dealer);

        // then
        assertThat(dealer.calculateCardScore() > sumBeforeDrawn).isTrue();
    }

    @Test
    @DisplayName("딜러가 뽑은 카드의 총합이 16이하라면 true를 반환한다.")
    void returns_true_when_dealer_cards_sum_under_boundary() {
        // given
        Card card1 = new Card(Type.CLUB, Value.EIGHT);
        Card card2 = new Card(Type.SPADE, Value.EIGHT);
        List<Card> givenCards = List.of(card1, card2);

        DrawnCards drawnCards = new DrawnCards(givenCards);

        Dealer dealer = new Dealer(drawnCards);

        // when
        boolean result = blackJackService.canDealerDrawMore(dealer);

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("딜러와 플레이어의 게임 결과를 반환한다.")
    void returns_dealer_and_players_result() {
        // given
        DrawnCards emptyCards = new DrawnCards(new ArrayList<>());

        Player player = new Player(new Name("pobi"), emptyCards);
        Players players = new Players(List.of(player));

        Dealer dealer = new Dealer(emptyCards);

        // when
        List<ParticipantResult> result = blackJackService.getParticipantResults(dealer, players);

        // then
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0).getName()).isEqualTo(dealer.getName());
        assertThat(result.get(0).getScore()).isEqualTo(dealer.calculateCardScore());
        assertThat(result.get(1).getName()).isEqualTo(player.getName());
        assertThat(result.get(1).getScore()).isEqualTo(player.calculateCardScore());
    }

    @Test
    @DisplayName("플레이어의 블랙잭 게임 승패 결과를 반환해준다.")
    void returns_players_win_lose_result() {
        // given
        Card card1 = new Card(Type.CLUB, Value.EIGHT);
        Card card2 = new Card(Type.SPADE, Value.EIGHT);

        DrawnCards drawnCards = new DrawnCards(List.of(card1, card2));
        DrawnCards emptyCards = new DrawnCards(new ArrayList<>());

        Player player = new Player(new Name("pobi"), emptyCards);
        Players players = new Players(List.of(player));
        Dealer dealer = new Dealer(drawnCards);

        // when
        List<WinLoseResult> result = blackJackService.getWinLoseResults(dealer, players);

        // then
        assertThat(result.get(0).getName()).isEqualTo(player.getName());
        assertThat(result.get(0).isWin()).isFalse();
    }

    @Test
    @DisplayName("딜러의 블랙잭 게임의 승패 결과를 반환해준다.")
    void returns_dealer_win_lose_result() {
        // given
        Card card1 = new Card(Type.CLUB, Value.EIGHT);
        Card card2 = new Card(Type.SPADE, Value.EIGHT);

        DrawnCards drawnCards = new DrawnCards(List.of(card1, card2));
        DrawnCards emptyCards = new DrawnCards(new ArrayList<>());

        Player player = new Player(new Name("pobi"), emptyCards);
        Players players = new Players(List.of(player));
        Dealer dealer = new Dealer(drawnCards);

        List<WinLoseResult> winLoseResults = blackJackService.getWinLoseResults(dealer, players);

        // when
        DealerWinLoseResult dealerWinLoseResult = blackJackService.getDealerResult(winLoseResults, dealer);

        // then
        assertThat(dealerWinLoseResult.getName()).isEqualTo(Message.DEALER_NAME.getMessage());
        assertThat(dealerWinLoseResult.getWinCount()).isEqualTo(1);
        assertThat(dealerWinLoseResult.getLoseCount()).isEqualTo(0);
    }
}
