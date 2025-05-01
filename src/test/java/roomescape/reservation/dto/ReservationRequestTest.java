package roomescape.reservation.dto;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class ReservationRequestTest {

    @DisplayName("예약자명이 널 값일 경우 예외가 발생한다.")
    @Test
    void exception_name_null() {
        assertThatThrownBy(() -> new ReservationRequest(null, LocalDate.now(), 1L, 1L))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("예약자명이 빈 문자열이나 공백인 경우 예외가 발생한다.")
    @ValueSource(strings = {"", " "})
    @ParameterizedTest
    void exception_name_blank(String name) {
        assertThatThrownBy(() -> new ReservationRequest(name, LocalDate.now(), 1L, 1L))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("날짜가 널 값일 경우 예외가 발생한다.")
    @Test
    void exception_date_null() {
        assertThatThrownBy(() -> new ReservationRequest("riwon", null, 1L, 1L))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("날짜가 현재보다 이전일 경우 예외가 발생한다.")
    @Test
    void exception_date_before() {
        assertThatThrownBy(() -> new ReservationRequest("riwon", LocalDate.now().minusDays(1), 1L, 1L))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("시간 id가 널 값일 경우 예외가 발생한다.")
    @Test
    void exception_timeId_null() {
        assertThatThrownBy(() -> new ReservationRequest("riwon", LocalDate.now(), null, 1L))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테마 id가 널 값일 경우 예외가 발생한다.")
    @Test
    void exception_themeId_null() {
        assertThatThrownBy(() -> new ReservationRequest("riwon", LocalDate.now(), 1L, null))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
