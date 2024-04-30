package roomescape.controller.request;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.exception.BadRequestException;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ReservationRequestTest {

    @DisplayName("예약자명이 null인 경우 예외를 발생시킨다.")
    @Test
    void should_throw_exception_when_startAt_is_null() {
        assertThatThrownBy(() -> new ReservationRequest(LocalDate.of(2024, 4, 30), null, 1))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("[ERROR] 유효하지 않은 요청입니다.");
    }

    @DisplayName("예약자명이 빈 문자열인 경우 예외를 발생시킨다.")
    @Test
    void should_throw_exception_when_startAt_is_empty() {
        assertThatThrownBy(() -> new ReservationRequest(LocalDate.of(2024, 4, 30), "", 1))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("[ERROR] 유효하지 않은 요청입니다.");
    }
}
