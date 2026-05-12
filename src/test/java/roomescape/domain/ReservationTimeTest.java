package roomescape.domain;

import java.time.LocalTime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import roomescape.exception.RoomescapeException;
import roomescape.model.ReservationTime;

public class ReservationTimeTest {

    @Test
    public void 테마의_시작_시간은_정각이다() {
        // when & then
        int hour = 12;
        int minute = 0;
        ReservationTime reservationTime = new ReservationTime((long) 0, LocalTime.of(hour, minute));
        Assertions.assertEquals(minute, reservationTime.startAt().getMinute());
    }

    @Test
    public void 테마의_시작_시간이_정각이_아니면_예외가_발생한다() {
        // when & then
        Assertions.assertThrows(RoomescapeException.class, () ->
                new ReservationTime((long) 1, LocalTime.of(12, 30)));
    }
}
