package roomescape.reservation.controller.dto;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class ReservationRequestTest {

    @DisplayName("이름이 존재하지 않거나 공백이면 예외가 발생한다")
    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "\t", "\n"})
    void name_validate_exception(String name) {
        // given
        LocalDate date = LocalDate.of(2025, 4, 25);
        Long timeId = 1L;
        Long themeId = 1L;

        // when & then
        assertThatThrownBy(() -> new ReservationRequest(name, date, timeId, themeId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이름은 필수입니다.");
    }

    @DisplayName("예약 날짜가 존재하지 않으면 예외가 발생한다")
    @Test
    void date_null_exception() {
        // given
        String name = "루키";
        LocalDate date = null;
        Long timeId = 1L;
        Long themeId = 1L;

        // when & then
        assertThatThrownBy(() -> new ReservationRequest(name, date, timeId, themeId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("예약 날짜는 필수입니다.");
    }

    @DisplayName("예약 시간 ID 값이 존재하지 않으면 예외가 발생한다")
    @Test
    void time_id_null_exception() {
        // given
        String name = "루키";
        LocalDate date = LocalDate.of(2025, 4, 25);
        Long timeId = null;
        Long themeId = 1L;

        // when & then
        assertThatThrownBy(() -> new ReservationRequest(name, date, timeId, themeId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("예약 시간 ID는 필수입니다.");
    }

    @DisplayName("테마 ID 값이 존재하지 않으면 예외가 발생한다")
    @Test
    void theme_id_validate_test() {
        // given
        String name = "루키";
        LocalDate date = LocalDate.of(2025, 4, 25);
        Long timeId = 1L;
        Long themeId = null;

        // when & then
        assertThatThrownBy(() -> new ReservationRequest(name, date, timeId, themeId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("테마 ID는 필수입니다.");
    }


}
