package roomescape.time.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.format.DateTimeParseException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class ReservationTimeTest {

    @DisplayName("존재하지 않는 시간을 선택했을 경우 예외가 발생한다")
    @Test
    void validateTimeExist() {
        assertAll(
                () -> assertThatThrownBy(() -> new ReservationTime(1L, null))
                        .isInstanceOf(NullPointerException.class),
                () -> assertThatThrownBy(() -> new ReservationTime(1L, "25:00"))
                        .isInstanceOf(DateTimeParseException.class),
                () -> assertThatThrownBy(() -> new ReservationTime(1L, "15:68"))
                        .isInstanceOf(DateTimeParseException.class)
        );
    }
}
