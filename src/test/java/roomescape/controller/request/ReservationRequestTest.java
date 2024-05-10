package roomescape.controller.request;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import roomescape.exception.BadRequestException;

class ReservationRequestTest {

    @DisplayName("아이디가 0이하인 경우 예외를 발생시킨다.")
    @Test
    void should_throw_exception_when_id() {
            assertThatThrownBy(() -> new ReservationRequest(LocalDate.of(2030, 8, 9), 1L, 1L, 0L))
                    .isInstanceOf(BadRequestException.class)
                    .hasMessage("[ERROR] userId의 값이 \"0\"일 수 없습니다.");
    }
}
