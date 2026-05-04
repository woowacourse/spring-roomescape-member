package roomescape.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.time.LocalTime;
import org.junit.jupiter.api.Test;

public class ReservationTimeTest {

    @Test
    void idNullExceptionTest() {
        assertThatThrownBy(() -> new ReservationTime(null, LocalTime.of(10, 0)))
                .hasMessage("[ERROR] id는 비어 있을 수 없습니다.")
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void timeNullExceptionTest() {
        assertThatThrownBy(() -> new ReservationTime(1L, null))
                .hasMessage("[ERROR] 예약 시간은 비어 있을 수 없습니다.")
                .isInstanceOf(IllegalArgumentException.class);
    }
}
