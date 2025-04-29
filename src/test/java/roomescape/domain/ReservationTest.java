package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.exception.NotCorrectDateTimeException;

public class ReservationTest {

    @Test
    @DisplayName("현재 날짜 이전은 예약이 불가능하다.")
    void validateDate() {
        ReservationTime time = new ReservationTime(1L, LocalTime.of(10, 0));

        assertThatThrownBy(() -> new Reservation(1L, "사나", LocalDate.of(2020, 1, 1), time))
            .isInstanceOf(NotCorrectDateTimeException.class)
            .hasMessage("지나간 날짜와 시간에 대한 예약 생성은 불가능합니다.");
    }

    @Test
    @DisplayName("현재 시간 이전은 예약이 불가능하다.")
    void validateTime() {
        ReservationTime time = new ReservationTime(1L, LocalTime.MIN);

        assertThatThrownBy(() -> new Reservation(1L, "사나", LocalDate.now(), time))
            .isInstanceOf(NotCorrectDateTimeException.class)
            .hasMessage("지나간 날짜와 시간에 대한 예약 생성은 불가능합니다.");
    }
}
