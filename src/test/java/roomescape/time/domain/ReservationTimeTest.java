package roomescape.time.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.format.DateTimeParseException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ReservationTimeTest {

    @DisplayName("존재하지 않는 시간을 선택했을 경우 예외가 발생한다")
    @Test
    void validateTimeExist() {
        assertAll(
                () -> assertThatThrownBy(() -> ReservationTime.of(1L, null))
                        .isInstanceOf(NullPointerException.class),
                () -> assertThatThrownBy(() -> ReservationTime.of(1L, "25:00"))
                        .isInstanceOf(DateTimeParseException.class),
                () -> assertThatThrownBy(() -> ReservationTime.of(1L, "15:68"))
                        .isInstanceOf(DateTimeParseException.class)
        );
    }
}
