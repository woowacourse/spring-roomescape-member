package roomescape.domain_entity;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ReservationTest {

    private final String TEST_NAME = "moda";

    @Test
    @DisplayName("지난 날짜 예약일 경우 예외가 발생한다")
    void failIfPastDate() {
        //given
        LocalDateTime now = LocalDateTime.now();
        Reservation reservation = new Reservation(
                new Id(1L),
                TEST_NAME,
                now.toLocalDate().minusDays(1),
                new ReservationTime(now.toLocalTime()),
                new Theme("moda", "description", "thumbnail")
        );

        //when & then
        assertThatThrownBy(() -> reservation.validatePastDateTime())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("지난 날짜와 시간의 예약은 생성 불가능합니다.");
    }

    @Test
    @DisplayName("같은 날짜와 지난 시간 예약일 경우 예외가 발생한다")
    void failIfPastTime() {
        //given
        LocalDateTime now = LocalDateTime.now();
        Reservation reservation = new Reservation(
                new Id(1L),
                TEST_NAME,
                now.toLocalDate(),
                new ReservationTime(now.toLocalTime().minusMinutes(1)),
                new Theme("moda", "description", "thumbnail")
        );

        //when & then
        assertThatThrownBy(() -> reservation.validatePastDateTime())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("지난 날짜와 시간의 예약은 생성 불가능합니다.");
    }

    @Test
    @DisplayName("같은 날짜와 같은 시간 예약일 경우 예외가 발생하지 않는다.")
    void successIfSameDateAndSameTime() {
        //given
        LocalDateTime now = LocalDateTime.now();
        Reservation reservation = new Reservation(
                new Id(1L),
                TEST_NAME,
                now.toLocalDate(),
                new ReservationTime(now.toLocalTime()),
                new Theme("moda", "description", "thumbnail")
        );

        //when & then
        assertThatNoException().isThrownBy(reservation::validatePastDateTime);
    }

    @Test
    @DisplayName("미래 날짜 예약일 경우 예외가 발생하지 않는다.")
    void successIfFutureDate() {
        //given
        LocalDateTime now = LocalDateTime.now();
        Reservation reservation = new Reservation(
                new Id(1L),
                TEST_NAME,
                now.toLocalDate().plusDays(1),
                new ReservationTime(now.toLocalTime()),
                new Theme("moda", "description", "thumbnail")
        );

        //when & then
        assertThatNoException().isThrownBy(reservation::validatePastDateTime);
    }
}
