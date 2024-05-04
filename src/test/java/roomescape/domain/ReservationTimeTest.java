package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;

class ReservationTimeTest {

    @Test
    @DisplayName("예약 시간을 생성한다.")
    void create() {
        assertThatCode(() -> new ReservationTime(LocalTime.of(10, 0)))
                .doesNotThrowAnyException();
    }

    @ParameterizedTest
    @DisplayName("시작 시간이 없으면 예외가 발생한다.")
    @NullSource
    void validateStartAt(LocalTime startAt) {
        assertThatThrownBy(() -> new ReservationTime(startAt))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("시작 시간은 필수 값입니다.");
    }
}
