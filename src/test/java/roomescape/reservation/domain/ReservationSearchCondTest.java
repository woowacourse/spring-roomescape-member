package roomescape.reservation.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ReservationSearchCondTest {

    @DisplayName("기간이 30일을 넘으면 예외가 발생한다.")
    @Test
    void validateDateInterval() {
        LocalDate dateFrom = LocalDate.parse("2024-05-01");
        LocalDate dateTo = LocalDate.parse("2024-06-01");

        assertThatThrownBy(() -> new ReservationSearchCond(1L, 1L, dateFrom, dateTo))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
