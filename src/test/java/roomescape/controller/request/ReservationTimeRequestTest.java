package roomescape.controller.request;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import roomescape.exception.BadRequestException;

import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ReservationTimeRequestTest {

    @DisplayName("요청된 시간이 null인 경우 예외를 발생시킨다.")
    @Test
    void should_throw_exception_when_startAt_is_null() {
        assertThatThrownBy(() -> new ReservationTimeRequest(null))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("[ERROR] 요청된 데이터에 null 혹은 비어있는 값이 존재합니다.");
    }

    @DisplayName("요청된 시간이 유효한 경우 예외가 발생하지 않는다.")
    @ParameterizedTest
    @ValueSource(strings = {"10:00", "23:59:59", "12:00:00", "00:00:00"})
    void should_not_throw_exception_when_startAt_is_good_format(String startAt) {
        assertThatCode(() -> new ReservationTimeRequest(LocalTime.parse(startAt)))
                .doesNotThrowAnyException();
    }
}
