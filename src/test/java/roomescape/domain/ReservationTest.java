package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.Test;

class ReservationTest {

    @Test
    public void validateName() {
        assertThatThrownBy(
                () -> new Reservation(1L, "", LocalDate.of(2024, 4, 29),
                        new ReservationTime(1L, LocalTime.now()),
                        new Theme(1L, "테니", "설명", "썸네일")))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void validateDate() {
        assertThatThrownBy(
                () -> new Reservation(1L, "테니", null, new ReservationTime(1L, LocalTime.now()),
                        new Theme(1L, "테니", "설명", "썸네일")))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
