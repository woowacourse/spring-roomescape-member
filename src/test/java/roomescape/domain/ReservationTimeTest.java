package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;

class ReservationTimeTest {
    @ParameterizedTest
    @NullSource
    @DisplayName("예약 시간이 null이면 예외가 발생한다")
    void validate_time_fail(LocalTime time) {
        // given
        Long id = 1L;

        // when & then
        assertThatThrownBy(() -> new ReservationTime(id, time))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("예약 시간이 유효하지 않습니다.");
    }
}
