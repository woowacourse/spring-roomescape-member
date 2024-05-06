package roomescape.reservation.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import roomescape.reservation.handler.exception.CustomException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ReservationTest {

    private final ReservationTime reservationTime = new ReservationTime(1L, LocalTime.of(12, 30));
    private final Theme theme = new Theme(1L, "themeName", "description", "thumbnail");

    @DisplayName("실패: 이름은 1자 이상 10자 이하여야 한다")
    @ParameterizedTest
    @ValueSource(strings = {"", "01234567890"})
    void validateNameFailTest(String name) {
        assertThatThrownBy(() -> {
            new Reservation(1L, name, LocalDate.of(2999, 12, 12), reservationTime, theme);
        }).isInstanceOf(CustomException.class);
    }

    @DisplayName("성공: 이름은 1자 이상 10자 이하여야 한다")
    @ParameterizedTest
    @ValueSource(strings = {"0", "0123456789"})
    void validateNameSuccessTest(String name) {
        assertThatCode(() -> {
            new Reservation(1L, name, LocalDate.of(2999, 12, 12), reservationTime, theme);
        }).doesNotThrowAnyException();
    }

    @DisplayName("같은 날짜/시간임을 확인할 수 있다.")
    @Test
    void sameDateTimeTest() {
        Reservation reservation1 = new Reservation(1L, "name", LocalDate.of(2999, 12, 12), reservationTime, theme);
        Reservation reservation2 = new Reservation(2L, "name", LocalDate.of(2999, 12, 12), reservationTime, theme);

        assertThat(reservation1.isSameDateTime(reservation2)).isTrue();
    }

    @DisplayName("다른 날짜임을 확인할 수 있다.")
    @Test
    void differentDateTest() {
        Reservation reservation1 = new Reservation(1L, "name", LocalDate.of(2999, 12, 12), reservationTime, theme);
        Reservation reservation2 = new Reservation(2L, "name", LocalDate.of(2999, 12, 11), reservationTime, theme);

        assertThat(reservation1.isSameDateTime(reservation2)).isFalse();
    }

    @DisplayName("다른 시간임을 확인할 수 있다.")
    @Test
    void differentTimeTest() {
        ReservationTime otherReservationTime = new ReservationTime(2L, LocalTime.of(11,10));
        Reservation reservation1 = new Reservation(1L, "name", LocalDate.of(2999, 12, 12), otherReservationTime, theme);
        Reservation reservation2 = new Reservation(2L, "name", LocalDate.of(2999, 12, 12), reservationTime, theme);

        assertThat(reservation1.isSameDateTime(reservation2)).isFalse();
    }

    @DisplayName("예약 시간을 다른 시간과 비교할 수 있다.")
    @Test
    void pastTimeTest() {
        LocalDateTime localDateTime = LocalDateTime.of(2999, 12, 12, 12,30);
        Reservation reservation = new Reservation(1L, "name", LocalDate.of(2099, 12, 12), reservationTime, theme);

        assertThat(reservation.isBefore(localDateTime)).isTrue();
    }
}
