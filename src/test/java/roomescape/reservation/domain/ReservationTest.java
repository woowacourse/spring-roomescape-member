package roomescape.reservation.domain;


import java.time.LocalDate;
import java.time.LocalTime;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import roomescape.time.domain.ReservationTime;

class ReservationTest {

    @Test
    void 이름이_50자를_넘는_경우_예외가_발생한다() {
        //given
        String name = "a".repeat(51);
        ReservationTime reservationTime = new ReservationTime(LocalTime.now());
        LocalDate now = LocalDate.now();

        //when & then
        Assertions.assertThatThrownBy(() -> new Reservation(name, now, reservationTime))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
