package roomescape.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.Fixtures;

public class ReservationTest {

    @Test
    @DisplayName("예약 일시가 주어진 예약과 같은 일시인지 확인한다.")
    void isSameDateTime() {
        // given
        var reserveDate = Fixtures.getDateOfTomorrow();
        var reserveTime = Fixtures.JUNK_TIME_SLOT;
        var reserveTheme = Fixtures.JUNK_THEME;
        var reservation = new Reservation(1L, "리버", reserveDate, reserveTime, reserveTheme);
        var otherReservation = new Reservation(2L, "포포", reserveDate, reserveTime, reserveTheme);

        // when
        boolean isSameDateTime = reservation.isSameDateTime(otherReservation);

        // then
        assertThat(isSameDateTime).isTrue();
    }
}
