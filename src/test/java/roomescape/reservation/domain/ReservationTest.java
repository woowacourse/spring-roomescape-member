package roomescape.reservation.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import roomescape.reservation.domain.exception.InvalidReservationException;

class ReservationTest {

    @DisplayName("예약자 이름이 비어있을 때 예외 발생을 테스트합니다.")
    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" "})
    void validate_name(String name) {
        assertThatThrownBy(() -> Reservation.builder()
                .name(name)
                .date(LocalDate.of(2026, 5, 6))
                .themeId(1L)
                .timeId(1L)
                .build())
                .isInstanceOf(InvalidReservationException.class)
                .hasMessage("이름은 비어있을 수 없습니다.");
    }

    @DisplayName("예약 날짜가 비어있을 때 예외 발생을 테스트합니다.")
    @Test
    void validate_date() {
        assertThatThrownBy(() -> Reservation.builder()
                .name("스타크")
                .date(null)
                .themeId(1L)
                .timeId(1L)
                .build())
                .isInstanceOf(InvalidReservationException.class)
                .hasMessage("날짜는 비어있을 수 없습니다.");
    }

    @DisplayName("테마 ID가 올바르지 않을 때 예외 발생을 테스트합니다.")
    @ParameterizedTest
    @NullSource
    @ValueSource(longs = {0L, -1L})
    void validate_theme_id(Long themeId) {
        assertThatThrownBy(() -> Reservation.builder()
                .name("스타크")
                .date(LocalDate.of(2026, 5, 6))
                .themeId(themeId)
                .timeId(1L)
                .build())
                .isInstanceOf(InvalidReservationException.class)
                .hasMessage("테마ID는 올바른 값이어야 합니다.");
    }

    @DisplayName("시간 ID가 올바르지 않을 때 예외 발생을 테스트합니다.")
    @ParameterizedTest
    @NullSource
    @ValueSource(longs = {0L, -1L})
    void validate_time_id(Long timeId) {
        assertThatThrownBy(() -> Reservation.builder()
                .name("스타크")
                .date(LocalDate.of(2026, 5, 6))
                .themeId(1L)
                .timeId(timeId)
                .build())
                .isInstanceOf(InvalidReservationException.class)
                .hasMessage("시간ID는 올바른 값이어야 합니다.");
    }

    @DisplayName("현재 시간보다 이전 예약 시간일 때 예외 발생을 테스트합니다.")
    @Test
    void validate_past_reservation_date_time() {
        Reservation reservation = Reservation.builder()
                .name("스타크")
                .date(LocalDate.of(2026, 5, 6))
                .themeId(1L)
                .timeId(1L)
                .build();

        assertThatThrownBy(() -> reservation.validateNotPast(
                LocalTime.of(10, 0),
                LocalDateTime.of(2026, 5, 6, 11, 0)
        ))
                .isInstanceOf(InvalidReservationException.class)
                .hasMessage("현재 시간보다 이전 시간으로 예약을 할 수 없습니다.");
    }
}
