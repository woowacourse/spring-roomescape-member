package roomescape.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalTime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import roomescape.exception.ErrorCode;
import roomescape.exception.UnprocessableEntityException;
import roomescape.model.ReservationTime;

public class ReservationTimeTest {

    @Test
    public void 테마의_시작_시간은_정각이다() {
        // when & then
        int hour = 12;
        int minute = 0;
        ReservationTime reservationTime = new ReservationTime((long) 0, LocalTime.of(hour, minute));
        assertEquals(minute, reservationTime.getStartAt().getMinute());
    }

    @Test
    public void 테마의_시작_시간이_정각이_아니면_예외가_발생한다() {
        // when & then
        Assertions.assertThatThrownBy(() ->
                        new ReservationTime((long) 1, LocalTime.of(12, 30)))
                .isInstanceOf(UnprocessableEntityException.class)
                .hasMessageContaining(ErrorCode.TIME_NOT_ON_THE_HOUR.getMessage());
    }
}