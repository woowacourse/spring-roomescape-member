package roomescape.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import roomescape.domain.reservation.domain.Reservation;
import roomescape.global.exception.RoomEscapeException;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static roomescape.ReservationFixture.RESERVATION_TIME_NOW;
import static roomescape.ReservationFixture.THEME;

class ReservationTest {

    @ParameterizedTest
    @CsvSource(value = {",2024-05-04", "name,"})
    public void reservation_NullNameOrDate_ThrownException(String name, LocalDate date) {
        assertThatThrownBy(() -> new Reservation(0L, name, date, RESERVATION_TIME_NOW, THEME))
                .isInstanceOf(RoomEscapeException.class);
    }

    @Test
    public void reservation_NullReservationTime_ThrownException() {
        assertThatThrownBy(() -> new Reservation(0L, "테니", LocalDate.now(), null, THEME))
                .isInstanceOf(RoomEscapeException.class);
    }

    @Test
    public void reservation_NullTheme_ThrownException() {
        assertThatThrownBy(() -> new Reservation(0L, "테니", LocalDate.now(), RESERVATION_TIME_NOW, null))
                .isInstanceOf(RoomEscapeException.class);
    }
}
