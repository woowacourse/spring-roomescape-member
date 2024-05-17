package roomescape.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.exceptions.InvalidRequestBodyFieldException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ReservationTimeTest {

    @Test
    @DisplayName("시간 입력이 올바르지 않으면 예외가 발생한다.")
    void nullReservationTime() {
        assertThatThrownBy(() -> new ReservationTime(null))
                .isInstanceOf(InvalidRequestBodyFieldException.class);
    }
}
