package roomescape.reservation.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import roomescape.fixture.ThemeFixture;
import roomescape.global.exception.ConflictException;
import roomescape.global.exception.RoomEscapeException;
import roomescape.reservationtime.application.dto.ReservationTimeResult;

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
                .isInstanceOf(RoomEscapeException.class)
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
                .isInstanceOf(RoomEscapeException.class)
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
                .isInstanceOf(RoomEscapeException.class)
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
                .isInstanceOf(RoomEscapeException.class)
                .hasMessage("시간ID는 올바른 값이어야 합니다.");
    }

    @DisplayName("날짜와 시간을 다른 값으로 변경하면 변경된 예약을 반환합니다.")
    @Test
    void update_date_and_time() {
        Reservation reservation = Reservation.builder()
                .id(1L)
                .name("스타크")
                .date(LocalDate.of(2026, 5, 6))
                .themeId(1L)
                .timeId(1L)
                .build();

        Reservation updated = reservation.updateDateAndTime(LocalDate.of(2028, 6, 1), 2L);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(updated.getDate()).isEqualTo(LocalDate.of(2028, 6, 1));
            softly.assertThat(updated.getTimeId()).isEqualTo(2L);
        });
    }

    @DisplayName("동일한 날짜와 시간으로 변경 시 예외 발생을 테스트합니다.")
    @Test
    void update_date_and_time_same_value_exception() {
        Reservation reservation = Reservation.builder()
                .id(1L)
                .name("스타크")
                .date(LocalDate.of(2026, 5, 6))
                .themeId(1L)
                .timeId(1L)
                .build();

        assertThatThrownBy(() -> reservation.updateDateAndTime(LocalDate.of(2026, 5, 6), 1L))
                .isInstanceOf(ConflictException.class)
                .hasMessage("동일한 날짜와 시간으로 변경할 수 없습니다.");
    }
}
