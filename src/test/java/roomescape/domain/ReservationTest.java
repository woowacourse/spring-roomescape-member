package roomescape.domain;

import org.junit.jupiter.api.Test;
import roomescape.exception.IllegalDateException;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ReservationTest {

    @Test
    public void validateDate() {
        assertThatThrownBy(() -> new Reservation(1L, "테니", LocalDate.of(2024, 4, 29), new ReservationTime(1L, LocalTime.now())))
                .isInstanceOf(IllegalDateException.class);
    }
}
