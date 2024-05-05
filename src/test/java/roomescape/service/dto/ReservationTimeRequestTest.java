package roomescape.service.dto;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class ReservationTimeRequestTest {

    @DisplayName("시작 시간이 입력되지 않으면 예외가 발생한다.")
    @ParameterizedTest
    @NullAndEmptySource
    void throw_exception_when_null_input(String startAt) {
        assertThatThrownBy(() -> new ReservationTimeRequest(startAt))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("시작 시간은 반드시 입력되어야 합니다.");
    }

    @DisplayName("잘못된 형식이 입력되면 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {"23", "25:00", "3:3", "3시 25분", "03:60"})
    void throw_exception_when_invalid_time_format_input(String startAt) {
        assertThatThrownBy(() -> new ReservationTimeRequest(startAt))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("시간 형식이 올바르지 않습니다.");
    }

    @DisplayName("유효한 시간 입력 시 정상 생성된다.")
    @ParameterizedTest
    @ValueSource(strings = {"00:00", "23:59", "05:05"})
    void create_success(String startAt) {
        assertThatNoException()
                .isThrownBy(() -> new ReservationTimeRequest(startAt));
    }
}
