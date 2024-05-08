package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ReservationTest {

    @Test
    @DisplayName("이름에 빈 값이 들어간 경우 IllegalArgumentException 발생")
    void nameEmptyException() {
        assertThatThrownBy(() -> new Reservation(
                0L, " ", LocalDate.now(),
                new ReservationTime(0, LocalTime.now()),
                new Theme(0, "theme", "description", "thumbnail"))
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Name cannot be empty");
    }

    @Test
    @DisplayName("이름이 10글자 초과일 경우 IllegalArgumentException 발생")
    void nameLengthException() {
        assertThatThrownBy(() -> new Reservation(
                0L, "namelength12", LocalDate.now(),
                new ReservationTime(0, LocalTime.now()),
                new Theme(0, "theme", "description", "thumbnail"))
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Name cannot exceed 10 characters");
    }
}
