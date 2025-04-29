package roomescape.reservation.dto;


import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ReservationRequestTest {
    // TODO : 검증 로직 테스트 추가

    @DisplayName("예약자명이 null일 경우 예외가 발생한다.")
    @Test
    void test1() {
        assertThatThrownBy(() -> new ReservationRequest(null, LocalDate.now(), 1L))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("날짜가 null일 경우 예외가 발생한다.")
    @Test
    void test2() {
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
