package roomescape.reservation.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.time.domain.ReservationTime;

import java.time.format.DateTimeParseException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class ReservationTest {

    @DisplayName("이름이 null 혹은 공백인 경우 예외가 발생한다")
    @Test
    void validateNameExist() {
        ReservationTime reservationTime = new ReservationTime(1L, "15:46");

        assertAll(
                () -> assertThatThrownBy(() -> new Reservation(1L, null, "2024-04-30", reservationTime))
                        .isInstanceOf(IllegalArgumentException.class),
                () -> assertThatThrownBy(() -> new Reservation(1L, "", "2024-04-30", reservationTime))
                        .isInstanceOf(IllegalArgumentException.class),
                () -> assertThatThrownBy(() -> new Reservation(1L, " ", "2024-04-30", reservationTime))
                        .isInstanceOf(IllegalArgumentException.class)
        );
    }

    @DisplayName("")
    @Test
    void validateDateAndTimeExist() {
        ReservationTime reservationTime = new ReservationTime(1L, "15:46");

        assertAll(
                () -> assertThatThrownBy(() -> new Reservation(1L, "hotea", null, reservationTime))
                        .isInstanceOf(IllegalArgumentException.class),
                () -> assertThatThrownBy(() -> new Reservation(1L, "hotea", "2024-14-30", reservationTime))
                        .isInstanceOf(DateTimeParseException.class),
                () -> assertThatThrownBy(() -> new Reservation(1L, "hotea", "2024-04-50", reservationTime))
                        .isInstanceOf(DateTimeParseException.class)
        );
    }
}