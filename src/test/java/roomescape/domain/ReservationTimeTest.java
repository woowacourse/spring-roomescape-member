package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import roomescape.exception.reservationtime.ReservationTimeFieldRequiredException;

class ReservationTimeTest {
    @DisplayName("startAt이 null이면 예외가 발생한다")
    @ParameterizedTest
    @NullSource
    void validateStartAt(LocalTime startAt) {
        // when // then
        assertThatThrownBy(() -> new ReservationTime(startAt))
                .isInstanceOf(ReservationTimeFieldRequiredException.class);
    }

}
