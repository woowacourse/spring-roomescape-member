package roomescape.domain;

import org.junit.jupiter.api.Test;
import roomescape.reservation.domain.ReservationTime;
import roomescape.global.exception.RoomEscapeException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ReservationTimeTest {

    @Test
    void validateStartAt() {
        assertThatThrownBy(() -> new ReservationTime(0L, null))
                .isInstanceOf(RoomEscapeException.class);
    }
}
