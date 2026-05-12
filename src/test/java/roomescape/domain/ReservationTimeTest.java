package roomescape.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import org.junit.jupiter.api.Test;

public class ReservationTimeTest {

    @Test
    void timeNullExceptionTest() {
        assertThatThrownBy(() -> new ReservationTime(1L, null))
                .hasMessage("[ERROR] 예약 시간은 비어 있을 수 없습니다.")
                .isInstanceOf(IllegalArgumentException.class);
    }
}
