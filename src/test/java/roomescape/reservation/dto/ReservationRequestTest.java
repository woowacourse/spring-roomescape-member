package roomescape.reservation.dto;


import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class ReservationRequestTest {
    // TODO : 검증 로직 테스트 추가

    @DisplayName("예약자명이 null일 경우 예외가 발생한다.")
    @Test
    void test1() {
        assertThatThrownBy(() -> new ReservationRequest(null, LocalDate.now(), 1L))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("예약자명이 빈 문자열이나 공백인 경우 예외가 발생한다.")
    @ValueSource(strings = {"", " "})
    @ParameterizedTest
    void test2(String name) {
        assertThatThrownBy(() -> new ReservationRequest(name, LocalDate.now(), 1L))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("날짜가 null일 경우 예외가 발생한다.")
    @Test
    void test5() {
        assertThatThrownBy(() -> new ReservationRequest("riwon", null, 1L))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("시간 id가 null일 경우 예외가 발생한다.")
    @Test
    void test3() {
        assertThatThrownBy(() -> new ReservationRequest("riwon", LocalDate.now(), null))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
