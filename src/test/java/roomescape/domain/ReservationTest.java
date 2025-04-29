package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ReservationTest {

    @Test
    @DisplayName("name이 null이면 예외를 던진다")
    void name_not_null() {
        assertThatThrownBy(() -> {
            new Reservation(
                null,
                LocalDate.of(2025, 10, 5),
                new ReservationTime(LocalTime.of(10, 5))
            );
        }).isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("date가 null이면 예외를 던진다")
    void date_not_null() {
        assertThatThrownBy(() -> {
            new Reservation(
                "두리",
                null,
                new ReservationTime(LocalTime.of(10, 5))
            );
        }).isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("time이 null이면 예외를 던진다")
    void time_not_null() {
        assertThatThrownBy(() -> {
            new Reservation(
                "두리",
                LocalDate.of(2025, 10, 5),
                null
            );
        }).isInstanceOf(NullPointerException.class);
    }

}
