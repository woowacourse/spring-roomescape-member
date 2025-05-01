package roomescape.unit.domain;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static roomescape.common.Constant.FIXED_CLOCK;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import roomescape.reservation.domain.ReservationDate;
import roomescape.reservation.domain.ReservationDateTime;
import roomescape.reservation.domain.exception.PastReservationException;
import roomescape.time.domain.ReservationTime;

public class ReservationDateTimeTest {

    @Test
    void 현재_이전_시간에는_예약할_수_없다() {
        LocalDateTime now = LocalDateTime.now(FIXED_CLOCK);
        ReservationDate today = new ReservationDate(now.toLocalDate());
        ReservationTime pastTime = new ReservationTime(1L, now.toLocalTime().minusHours(1));

        assertThatThrownBy(() -> new ReservationDateTime(today, pastTime, FIXED_CLOCK))
                .isInstanceOf(PastReservationException.class);
    }

    @Test
    void 현재_이후_시간에는_예약이_성공한다() {
        LocalDateTime now = LocalDateTime.now(FIXED_CLOCK);
        ReservationDate today = new ReservationDate(now.toLocalDate());
        ReservationTime futureTime = new ReservationTime(1L, now.toLocalTime().plusHours(1));

        assertThatCode(() -> new ReservationDateTime(today, futureTime, FIXED_CLOCK))
                .doesNotThrowAnyException();
    }

}
