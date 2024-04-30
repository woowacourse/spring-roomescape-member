package roomescape.controller.request;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.exception.BadRequestException;

class ReservationRequestTest {

    @DisplayName("시간이 null인 경우 예외를 발생시킨다.")
    @Test
    void should_throw_exception_when_startAt_is_null() {
        Assertions.assertThatThrownBy(() -> new ReservationTimeRequest(null))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("[ERROR] 유효하지 않은 요청입니다.");
    }
}