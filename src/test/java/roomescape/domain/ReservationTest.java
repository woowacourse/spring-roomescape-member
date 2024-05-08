package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservationtime.ReservationTime;
import roomescape.domain.theme.Theme;

class ReservationTest {

    private static final ReservationTime RESERVATION_TIME = new ReservationTime(LocalTime.of(10, 0));
    private static final Theme THEME = new Theme("name", "description", "thumbnail");
    private static final LocalDate DATE = LocalDate.of(2024, 5, 5);

    @Test
    @DisplayName("예약을 생성한다.")
    void create() {
        assertThatCode(() -> new Reservation("name", DATE, RESERVATION_TIME, THEME))
                .doesNotThrowAnyException();
    }

    @ParameterizedTest
    @DisplayName("이름이 공백이면 예외가 발생한다.")
    @NullAndEmptySource
    @ValueSource(strings = {" "})
    void validateName(String name) {
        assertThatThrownBy(() -> new Reservation(name, DATE, RESERVATION_TIME, THEME))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이름은 필수 값입니다.");
    }

    @Test
    @DisplayName("이름이 255자를 넘으면 예외가 발생한다.")
    void validateNameLength() {
        String name = "a".repeat(256);

        assertThatThrownBy(() -> new Reservation(name, DATE, RESERVATION_TIME, THEME))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이름은 255자를 넘을 수 없습니다.");
    }

    @Test
    @DisplayName("날짜가 없으면 예외가 발생한다.")
    void validateDate() {
        assertThatThrownBy(() -> new Reservation("name", null, RESERVATION_TIME, THEME))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("날짜는 필수 값입니다.");
    }

    @Test
    @DisplayName("예약 시간이 없으면 예외가 발생한다.")
    void validateTime() {
        assertThatThrownBy(() -> new Reservation("name", DATE, null, THEME))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("예약 시간은 필수 값입니다.");
    }

    @Test
    @DisplayName("테마가 없으면 예외가 발생한다.")
    void validateTheme() {
        assertThatThrownBy(() -> new Reservation("name", DATE, RESERVATION_TIME, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("테마는 필수 값입니다.");
    }
}
