package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import roomescape.domain.reservation.ReservationTime;

class ReservationTimeTest {

    @Test
    @DisplayName("예약 시간이 정상적으로 생성된다.")
    void createReservationTime() {
        // given
        final String time = "13:00";

        // when & then
        assertThatCode(() -> new ReservationTime(time))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("예약 시간이 10분 단위가 아니면 예외가 발생한다.")
    void throwExceptionWhenInvalidTimeUnit() {
        // given
        final String invalidTime = "13:01";

        // when & then
        assertThatThrownBy(() -> new ReservationTime(invalidTime))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", "13-00"})
    @DisplayName("예약 시간 입력 값이 유효하지 않으면 예외가 발생한다.")
    void throwExceptionWhenCannotConvertToLocalTime(final String invalidTime) {
        // when & then
        assertThatThrownBy(() -> new ReservationTime(invalidTime))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
