package roomescape.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import roomescape.reservation.domain.Reservation;
import roomescape.global.exception.RoomEscapeException;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static roomescape.ReservationFixture.*;

class ReservationTest {

    @ParameterizedTest
    @NullSource
    public void reservation_NullDate_ThrownException(LocalDate date) {
        assertThatThrownBy(() -> new Reservation(0L, RESERVATION_MEMBER, date, RESERVATION_TIME_NOW, THEME1))
                .isInstanceOf(RoomEscapeException.class);
    }

    @Test
    public void reservation_NullReservationTime_ThrownException() {
        assertThatThrownBy(() -> new Reservation(0L, RESERVATION_MEMBER, LocalDate.now(), null, THEME1))
                .isInstanceOf(RoomEscapeException.class);
    }

    @Test
    public void reservation_NullTheme_ThrownException() {
        assertThatThrownBy(() -> new Reservation(0L, RESERVATION_MEMBER, LocalDate.now(), RESERVATION_TIME_NOW, null))
                .isInstanceOf(RoomEscapeException.class);
    }
}
