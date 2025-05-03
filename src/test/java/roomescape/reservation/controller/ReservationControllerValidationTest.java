package roomescape.reservation.controller;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class ReservationControllerValidationTest {

    private ReservationController reservationController = new ReservationController(null);

    private static Stream<Arguments> invalidInputProvider() {
        return Stream.of(
                Arguments.of(null, null, "예약 날짜는 필수입니다."),
                Arguments.of(LocalDate.of(2025, 5, 14), null, "테마 ID는 필수입니다."),
                Arguments.of(null, 1L, "예약 날짜는 필수입니다.")
        );
    }

    @DisplayName("이용 가능한 시간 목록 요청 시 입력한 요청값이 없거나 공백이면 예외가 발생한다")
    @ParameterizedTest
    @MethodSource("invalidInputProvider")
    void get_available_times_request_validate_test(LocalDate date, Long themeId, String errorMessage) {
        // when & then
        assertThatThrownBy(() -> reservationController.getAvailableTimes(date, themeId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(errorMessage);
    }
}
