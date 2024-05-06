package roomescape.core.domain;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static roomescape.util.Fixture.DATE;
import static roomescape.util.Fixture.ID;
import static roomescape.util.Fixture.RESERVATION_TIME;
import static roomescape.util.Fixture.THEME;
import static roomescape.util.Fixture.USER_NAME;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class ReservationTest {
    @Test
    @DisplayName("예약을 생성한다")
    void createReservation() {
        assertThatCode(() -> new Reservation(ID, USER_NAME, DATE, RESERVATION_TIME, THEME))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("예약 생성 시, id가 null이면 예외가 발생한다")
    void throwExceptionWhenNullId() {
        assertThatThrownBy(() -> new Reservation(null, USER_NAME, DATE, RESERVATION_TIME, THEME))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("예약 id는 null일 수 없습니다.");
    }

    @ParameterizedTest
    @DisplayName("예약 생성 시, name이 null이나 빈값이면 예외가 발생한다")
    @NullAndEmptySource
    @ValueSource(strings = {" ", "\n"})
    void throwExceptionWhenEmptyName(String emptyName) {
        assertThatThrownBy(() -> new Reservation(ID, emptyName, DATE, RESERVATION_TIME, THEME))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("예약 이름은 null이나 빈 값일 수 없습니다.");
    }

    @ParameterizedTest
    @DisplayName("예약 생성 시, date가 null이나 빈값이면 예외가 발생한다")
    @NullAndEmptySource
    @ValueSource(strings = {" ", "\n"})
    void throwExceptionWhenEmptyDate(String emptyDate) {
        assertThatThrownBy(() -> new Reservation(ID, USER_NAME, emptyDate, RESERVATION_TIME, THEME))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("예약 날짜는 null이나 빈 값일 수 없습니다.");
    }

    @Test
    @DisplayName("예약 생성 시, date가 유효한 날짜 형식이 아니면 예외가 발생한다")
    void throwExceptionWhenInvalidDateFormat() {
        assertThatThrownBy(() -> new Reservation(ID, USER_NAME, "2024/5/5", RESERVATION_TIME, THEME))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("예약 날짜 형식이 잘못되었습니다.");
    }

    @Test
    @DisplayName("예약 생성 시, reservationTime이 null이 들어오면 예외가 발생한다.")
    void throwExceptionWhenNullReservationTime() {
        assertThatThrownBy(() -> new Reservation(ID, USER_NAME, DATE, null, THEME))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("예약 시간은 null일 수 없습니다.");
    }

    @Test
    @DisplayName("예약 생성 시, theme가 null이 들어오면 예외가 발생한다.")
    void throwExceptionWhenNullTheme() {
        assertThatThrownBy(() -> new Reservation(ID, USER_NAME, DATE, RESERVATION_TIME, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("예약 테마는 null일 수 없습니다.");
    }
}
