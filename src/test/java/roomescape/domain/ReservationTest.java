package roomescape.domain;

import java.time.LocalDate;
import java.time.LocalTime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ReservationTest {
    @Test
    @DisplayName("날짜를 기준으로 비교를 잘 하는지 확인.")
    void compareTo() {
        Reservation first = new Reservation(1L, "폴라", LocalDate.of(1999, 12, 1), new ReservationTime(
                LocalTime.of(16, 30)));
        Reservation second = new Reservation(2L, "로빈", LocalDate.of(1998, 1, 8), new ReservationTime(
                LocalTime.of(16, 30)));
        int compareTo = first.compareTo(second);
        Assertions.assertThat(compareTo)
                .isGreaterThan(0);
    }
}
