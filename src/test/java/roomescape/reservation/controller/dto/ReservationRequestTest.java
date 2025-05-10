package roomescape.reservation.controller.dto;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.global.exception.error.InvalidRequestException;

class ReservationRequestTest {

    @DisplayName("예약 날짜가 존재하지 않으면 예외가 발생한다")
    @Test
    void date_null_exception() {
        // given
        LocalDate date = null;
        Long timeId = 1L;
        Long themeId = 1L;
        Long memberId = 1L;

        // when & then
        assertThatThrownBy(() -> new ReservationRequest(date, timeId, themeId, memberId))
                .isInstanceOf(InvalidRequestException.class)
                .hasMessage("예약 날짜는 필수입니다.");
    }

    @DisplayName("예약 시간 ID 값이 존재하지 않으면 예외가 발생한다")
    @Test
    void time_id_null_exception() {
        // given
        LocalDate date = LocalDate.of(2025, 4, 25);
        Long timeId = null;
        Long themeId = 1L;
        Long memberId = 1L;

        // when & then
        assertThatThrownBy(() -> new ReservationRequest(date, timeId, themeId, memberId))
                .isInstanceOf(InvalidRequestException.class)
                .hasMessage("예약 시간 ID는 필수입니다.");
    }

    @DisplayName("테마 ID 값이 존재하지 않으면 예외가 발생한다")
    @Test
    void theme_id_validate_test() {
        // given
        LocalDate date = LocalDate.of(2025, 4, 25);
        Long timeId = 1L;
        Long themeId = null;
        Long memberId = 1L;

        // when & then
        assertThatThrownBy(() -> new ReservationRequest(date, timeId, themeId, memberId))
                .isInstanceOf(InvalidRequestException.class)
                .hasMessage("테마 ID는 필수입니다.");
    }

}
