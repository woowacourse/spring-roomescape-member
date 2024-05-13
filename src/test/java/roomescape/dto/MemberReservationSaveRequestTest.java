package roomescape.dto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import roomescape.global.exception.RoomEscapeException;
import roomescape.reservation.dto.MemberReservationSaveRequest;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MemberReservationSaveRequestTest {

    @ParameterizedTest
    @NullSource
    public void reservation_NullDate_ThrownException(LocalDate date) {
        assertThatThrownBy(() -> new MemberReservationSaveRequest(date, 0L, 0L))
                .isInstanceOf(RoomEscapeException.class);
    }

    @Test
    public void reservation_NullReservationTime_ThrownException() {
        assertThatThrownBy(() -> new MemberReservationSaveRequest(LocalDate.now(), null, 0L))
                .isInstanceOf(RoomEscapeException.class);
    }

    @Test
    public void reservation_NullTheme_ThrownException() {
        assertThatThrownBy(() -> new MemberReservationSaveRequest(LocalDate.now(), 0L, null))
                .isInstanceOf(RoomEscapeException.class);
    }
}
