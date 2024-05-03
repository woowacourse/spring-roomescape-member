package roomescape.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import roomescape.domain.exception.InvalidDateException;
import roomescape.domain.exception.InvalidRequestException;

import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ReservationTest {

    @Test
    @DisplayName("빈 이름이 입력 될 경우 예외가 발생한다.")
    void validateEmptyName() {
        assertThatThrownBy(() -> new Reservation(
                2L,
                "",
                "2024-04-24",
                new ReservationTime(1L, LocalTime.of(10, 0)),
                new Theme(1L, null, null, null)
        )).isInstanceOf(InvalidRequestException.class);
    }

    @Test
    @DisplayName("빈 날짜가 입력 될 경우 예외가 발생한다.")
    void validateEmptyDate() {
        assertThatThrownBy(() -> new Reservation(
                2L,
                "seyang",
                "",
                new ReservationTime(1L, LocalTime.of(10, 0)),
                new Theme(1L, null, null, null)
        )).isInstanceOf(InvalidRequestException.class);
    }

    @ParameterizedTest
    @DisplayName("유효하지 않은 날짜인경우 예외가 발생한다.")
    @ValueSource(strings = {"202020-12-13", "1-13-4", "2024-14-15"})
    void validateFormat(String date) {
        final ReservationTime time = new ReservationTime(1L, LocalTime.parse("13:00"));
        final Theme theme = new Theme(1L, "spring", "Escape from spring cold", "Spring thumb");
        assertThatThrownBy(() -> new Reservation(1L, "name", date, time, theme))
                .isInstanceOf(InvalidDateException.class);
    }
}
