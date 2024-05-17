package roomescape.domain.reservation;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ReservationTest {

    @DisplayName("현재보다 이전 날짜와 시간이면 참을 반환한다.")
    @Test
    void return_true_when_previous_date_and_time() {
        boolean isPreviousDate = Reservation.isPreviousDate(LocalDate.parse("2024-05-15"),
                new ReservationTime(1L, "10:00"));

        assertThat(isPreviousDate).isTrue();
    }

}
