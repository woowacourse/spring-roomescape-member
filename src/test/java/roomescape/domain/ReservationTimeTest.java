package roomescape.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.time.LocalTime;
import org.junit.jupiter.api.Test;

public class ReservationTimeTest {

    @Test
    void timeNullExceptionTest() {
        assertThatThrownBy(() -> new ReservationTime(1L, null))
                .hasMessage("[ERROR] 예약 시간은 비어 있을 수 없습니다.")
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void checkPastTest() {
        LocalTime pastTime = LocalTime.of(7, 0);
        LocalTime futureTime = LocalTime.of(15, 0);

        ReservationTime reservationTime = new ReservationTime(1L, LocalTime.of(10, 0));

        assertThat(reservationTime.isPast(futureTime)).isTrue();
        assertThat(reservationTime.isPast(pastTime)).isFalse();
    }
}
