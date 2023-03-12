package domain;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import domain.card.Card;
import domain.card.DrawnCards;
import domain.message.ExceptionMessage;
import domain.participants.Account;
import domain.participants.Name;
import domain.participants.Player;
import domain.participants.Players;
import domain.participants.Status;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class PlayersTest {

    @DisplayName("Players를 정상적으로 생성한다.")
    @Test
    void create_success() {
        // given
        List<Player> players = createPlayers("pobi", "neo", "ori", "jay");

        // when && then
        assertThatNoException()
                .isThrownBy(() -> new Players(players));
    }

    private List<Player> createPlayers(String... names) {
        List<Card> cards = new ArrayList<>();
        List<Player> players = new ArrayList<>();
        for (String name : names) {
            players.add(new Player(new Status(new Name(name), new Account(10000)), new DrawnCards(cards)));
        }

        return players;
    }

    @Test
    @DisplayName("요구사항에 맞게 배팅 금액의 조건이 충족하면 정상적으로 객체를 생성한다.")
    void create_account_success() {
        // given
        Player player = new Player(new Status(new Name("pobi"), new Account(1000)), new DrawnCards(new ArrayList<>()));

        // when & then
        assertThatNoException()
                .isThrownBy(() -> new Players(List.of(player)));
    }

    @ParameterizedTest
    @ValueSource(ints = {-100, 0, 100, 900})
    @DisplayName("배팅 금액이 1000원 미만이면 예외가 발생한다.")
    void throws_exception_when_betting_account_under_boundary(int givenAccount) {
        // given
        Player player = new Player(new Status(new Name("pobi"), new Account(givenAccount)),
                new DrawnCards(new ArrayList<>()));

        // when & then
        assertThatThrownBy(() -> new Players(List.of(player)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ExceptionMessage.BETTING_MONEY_NEED_MORE.getMessage());
    }
}
