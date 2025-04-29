package roomescape.dto;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

class ReservationRequestDtoTest {

    @DisplayName("빈 이름이 들어온다면, 예외가 발생해야 한다")
    @ParameterizedTest
    @NullAndEmptySource
    void invalid_name_then_throw_exception(String text) {
        assertThatThrownBy(() -> new ReservationRequestDto(
            text,
            "2024-12-02",
            1L
        ));
    }

    @DisplayName("빈 날짜가 들어온다면, 예외가 발생해야 한다")
    @ParameterizedTest
    @NullAndEmptySource
    void invalid_date_then_throw_exception(String text) {
        assertThatThrownBy(() -> new ReservationRequestDto(
            "hi",
            text,
            1L
        ));
    }

    @Test
    @DisplayName("잘못된 날짜 형식이 들어온다면, 예외가 발생해야 한다")
    void invalid_date_format_then_throw_exception() {
        assertThatThrownBy(() -> new ReservationRequestDto(
            "hi",
            "2024-13-12",
            1L
        ));
    }

    @Test
    @DisplayName("빈 시간ID가 들어온다면, 예외가 발생해야 한다")
    void invalid_time_id_then_throw_exception() {
        assertThatThrownBy(() -> new ReservationRequestDto(
            "hi",
            "2024-12-02",
            null
        ));
    }

    @DisplayName("이름이 들어온다면, 성공한다.")
    @Test
    void success_name() {
        assertThatCode(() -> new ReservationRequestDto(
            "hi",
            "2024-12-02",
            1L
        ));
    }
}