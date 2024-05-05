package roomescape.dto;

import org.junit.jupiter.api.Test;
import roomescape.domain.ReservationTime;
import roomescape.exception.RoomEscapeException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TimeSaveRequestTest {

    @Test
    void validateStartAt() {
        assertThatThrownBy(() -> new ReservationTime(0L, null))
                .isInstanceOf(RoomEscapeException.class);
    }
}
