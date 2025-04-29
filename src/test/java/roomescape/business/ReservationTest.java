package roomescape.business;

import static org.assertj.core.api.Assertions.assertThatCode;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ReservationTest {

    @DisplayName("과거 일시로 예약을 생성할 경우 예외가 발생한다.")
    @Test
    void test() {
        // given
        LocalDateTime pastDateTime = LocalDateTime.now().minusDays(1);

        // when
        // then
        assertThatCode(() -> new Reservation("벨로", pastDateTime.toLocalDate(),
                new ReservationTime(pastDateTime.toLocalTime())))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("과거 일시로 예약을 생성할 수 없습니다.");
    }

}
