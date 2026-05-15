package roomescape.reservationtime.domain;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.Test;
import roomescape.exception.BusinessRuleException;

class ReservationTimeTest {

    @Test
    void 미래_날짜이면_검증을_통과한다() {
        ReservationTime time = new ReservationTime(1L, LocalTime.of(10, 0));

        assertThatCode(() -> time.validateFutureDate(LocalDate.now().plusDays(1)))
                .doesNotThrowAnyException();
    }

    @Test
    void 과거_날짜이면_예외가_발생한다() {
        ReservationTime time = new ReservationTime(1L, LocalTime.of(10, 0));

        assertThatThrownBy(() -> time.validateFutureDate(LocalDate.now().minusDays(1)))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessage("지나간 날짜·시간에는 예약할 수 없습니다.");
    }

    @Test
    void 오늘_날짜라도_지난_시간이면_예외가_발생한다() {
        ReservationTime time = new ReservationTime(1L, LocalTime.of(0, 0));

        assertThatThrownBy(() -> time.validateFutureDate(LocalDate.now()))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessage("지나간 날짜·시간에는 예약할 수 없습니다.");
    }
}