package roomescape.domain.reservation;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.common.exception.reservation.InvalidReservationException;

class ReservationDateTest {

    @DisplayName("현재 날짜 기준으로 이전 날짜면 예외가 발생한다.")
    @Test
    void before_date_then_exception() {
        LocalDate date = LocalDate.of(2025, 4, 28);
        LocalDate current = LocalDate.of(2025, 4, 29);

        ReservationDate reservationDate = new ReservationDate(date);
        assertThatThrownBy(() -> reservationDate.validateDate(current))
                .isInstanceOf(InvalidReservationException.class)
                .hasMessage("현재 날짜 기준으로 과거의 날짜는 예약할 수 없습니다.");
    }

    @DisplayName("현재 날짜 기준으로 같거나, 이후 날짜면 성공한다.")
    @Test
    void after_date_then_success() {
        LocalDate date = LocalDate.of(2025, 4, 28);
        LocalDate current = LocalDate.of(2025, 4, 27);

        ReservationDate reservationDate = new ReservationDate(date);
        assertThatCode(() -> reservationDate.validateDate(current))
                .doesNotThrowAnyException();
    }
}
