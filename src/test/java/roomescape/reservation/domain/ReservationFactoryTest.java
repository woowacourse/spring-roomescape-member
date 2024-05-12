package roomescape.reservation.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.domain.reservation.ReservationFactory;
import roomescape.domain.reservation.ReservationTheme;
import roomescape.domain.reservation.ReservationTime;

class ReservationFactoryTest {

    ReservationFactory reservationFactory = new ReservationFactory();

    @DisplayName("지난 날짜로는 생성할 수 없다.")
    @Test
    void createWithPastDate() {
        // given
        String name = "name";
        ReservationTime time = new ReservationTime(1L, LocalTime.now());
        ReservationTheme theme = new ReservationTheme(1L, "name", "desc", "thumb");

        // when
        LocalDate date = LocalDate.now().minusDays(1);

        // then
        assertThatThrownBy(() -> reservationFactory.createForAdd(name, date, time, theme))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("같은 날이어도 지난 시간으로는 생성할 수 없다.")
    @Test
    void createWithPastTime() {
        // given
        String name = "name";
        LocalDate date = LocalDate.now();
        ReservationTheme theme = new ReservationTheme(1L, "name", "desc", "thumb");

        // when
        ReservationTime time = new ReservationTime(1L, LocalTime.now().minusMinutes(1));

        // then
        assertThatThrownBy(() -> reservationFactory.createForAdd(name, date, time, theme))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
