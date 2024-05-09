package roomescape.dto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import roomescape.domain.reservation.dto.ReservationSaveRequest;
import roomescape.global.exception.RoomEscapeException;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ReservationSaveRequestTest {

    @ParameterizedTest
    @NullSource
    public void reservation_NullDate_ThrownException(LocalDate date) {
        assertThatThrownBy(() -> new ReservationSaveRequest(date, 0L, 0L))
                .isInstanceOf(RoomEscapeException.class);
    }

    @Test
    public void reservation_NullReservationTime_ThrownException() {
        assertThatThrownBy(() -> new ReservationSaveRequest(LocalDate.now(), null, 0L))
                .isInstanceOf(RoomEscapeException.class);
    }

    @Test
    public void reservation_NullTheme_ThrownException() {
        assertThatThrownBy(() -> new ReservationSaveRequest(LocalDate.now(), 0L, null))
                .isInstanceOf(RoomEscapeException.class);
    }
}
