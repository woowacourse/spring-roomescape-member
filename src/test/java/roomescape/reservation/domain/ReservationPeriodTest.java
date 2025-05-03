package roomescape.reservation.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ReservationPeriodTest {

    @Test
    @DisplayName("시작 날짜 계산 테스트")
    void find_start_date_test() {
        ReservationPeriod reservationPeriod = new ReservationPeriod(LocalDate.of(2000, 11, 5), 3, 1);

        LocalDate startDate = reservationPeriod.findStartDate();

        assertThat(startDate).isEqualTo(LocalDate.of(2000, 11, 2));
    }

    @Test
    @DisplayName("끝 날짜 계산 테스트")
    void find_end_date_test() {
        ReservationPeriod reservationPeriod = new ReservationPeriod(LocalDate.of(2000, 11, 5), 3, 1);

        LocalDate endDate = reservationPeriod.findEndDate();

        assertThat(endDate).isEqualTo(LocalDate.of(2000, 11, 4));
    }
}