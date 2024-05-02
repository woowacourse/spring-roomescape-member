package roomescape.domain;

import org.junit.jupiter.api.Test;
import roomescape.exception.IllegalDateException;
import roomescape.exception.IllegalTimeException;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ReservationTest {

    @Test
    public void validateDate() {
        assertThatThrownBy(() -> new Reservation(1L, "테니", LocalDate.of(2024, 4, 29), new ReservationTime(1L, LocalTime.now()), new Theme(1L, "테니", "설명", "썸네일")))
                .isInstanceOf(IllegalDateException.class);
    }

    @Test
    public void validateTime() {
        assertThatThrownBy(() -> new Reservation(1L, "테니", LocalDate.now(), new ReservationTime(1L, LocalTime.now().minusHours(1)), new Theme(1L, "테니", "설명", "썸네일")))
                .isInstanceOf(IllegalTimeException.class);

    }
}
