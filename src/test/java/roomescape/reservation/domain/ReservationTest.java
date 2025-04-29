package roomescape.reservation.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import roomescape.reservation.domain.exception.PastReservationException;
import roomescape.time.domain.ReservationTime;

public class ReservationTest {

    @ParameterizedTest
    @ValueSource(ints = {0, 1})
    void 현재나_이전_시간에는_예약할_수_없다(int minuteOffset) {
        LocalDate today = LocalDate.now();
        LocalTime time = LocalTime.now().minusMinutes(minuteOffset);
        ReservationTime reservationTime = ReservationTime.create(time);

        assertThatThrownBy(() -> Reservation.create(
                "홍길동", today, reservationTime))
                .isInstanceOf(PastReservationException.class);
    }

    @Test
    void 현재_이후_시간에는_예약이_성공한다() {
        LocalDate today = LocalDate.now();
        ReservationTime futureTime = ReservationTime.create(LocalTime.now().plusMinutes(10));

        assertThatCode(() -> Reservation.create(
                "홍길동", today, futureTime))
                .doesNotThrowAnyException();
    }

}
