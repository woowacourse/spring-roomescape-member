package roomescape.domain;

import java.time.LocalTime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import roomescape.model.ReservationTime;

public class ReservationTimeTest {

    @Test
    public void 테마의_시작_시간은_정각이다() {
        int hour = 12;
        int minute = 0;
        ReservationTime reservationTime = new ReservationTime((long) 0, LocalTime.of(hour, minute));
        Assertions.assertEquals(minute, reservationTime.startAt().getMinute());
    }
}
