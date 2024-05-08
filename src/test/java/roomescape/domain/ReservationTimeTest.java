package roomescape.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.domain.reservation.ReservationTime;

import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThatCode;

class ReservationTimeTest {
    @DisplayName("예약 시간을 생성한다")
    @Test
    void when_createReservationTime_then_created() {
        assertThatCode(() -> new ReservationTime(LocalTime.of(12, 12, 12)))
                .doesNotThrowAnyException();
    }
}
