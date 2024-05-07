package roomescape.time.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.exception.InvalidTimeException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ReservationTimeTest {

    @DisplayName("시간이 null인 경우 예외가 발생한다")
    @Test
    void validateTimeExist() {
        assertThatThrownBy(() -> new ReservationTime(1L, null))
                .isInstanceOf(InvalidTimeException.class);
    }
}