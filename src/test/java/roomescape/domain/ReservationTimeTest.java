package roomescape.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import roomescape.domain.exception.InvalidRequestException;
import roomescape.domain.exception.InvalidTimeException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ReservationTimeTest {

    @ParameterizedTest
    @ValueSource(strings = {"1:300", "112:33"})
    @DisplayName("유효하지 않은 시간을 입력할 경우 예외가 발생한다.")
    void validateTime(String time) {
        assertThatThrownBy(() -> new ReservationTime(time))
                .isInstanceOf(InvalidTimeException.class);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("빈 시간 요청인 경우 예외가 발생한다.")
    void validateEmpty(String value) {
        assertThatThrownBy(() -> new ReservationTime(value))
                .isInstanceOf(InvalidRequestException.class);
    }
}
