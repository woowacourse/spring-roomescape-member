package roomescape.reservation.domain;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.junit.jupiter.api.Test;
import roomescape.common.Constant;
import roomescape.reservation.domain.exception.PastReservationException;
import roomescape.time.domain.ReservationTime;

public class ReservationDateTimeTest {

    @Test
    void 현재_이전_시간에는_예약할_수_없다() {
        LocalDateTime now = LocalDateTime.now(Constant.FIXED_CLOCK);
        LocalDate today = now.toLocalDate();
        ReservationDate reservationDate = new ReservationDate(today);
        LocalTime time = now.toLocalTime().minusMinutes(1);
        ReservationTime reservationTime = ReservationTime.create(time);

        assertThatThrownBy(() -> new ReservationDateTime(reservationDate, reservationTime, Constant.FIXED_CLOCK))
                .isInstanceOf(PastReservationException.class);
    }

    @Test
    void 현재_이후_시간에는_예약이_성공한다() {
        LocalDateTime now = LocalDateTime.now(Constant.FIXED_CLOCK);
        LocalDate today = now.toLocalDate();
        ReservationDate reservationDate = new ReservationDate(today);
        ReservationTime futureTime = ReservationTime.create(now.toLocalTime().plusMinutes(10));

        assertThatCode(() -> new ReservationDateTime(reservationDate, futureTime, Constant.FIXED_CLOCK))
                .doesNotThrowAnyException();
    }
}
