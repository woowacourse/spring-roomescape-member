package roomescape;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.entity.ReservationTime;

class ReservationTimeTest {

    @Test
    @DisplayName("예약 가능한 시간이 아닐 때 예약하면 예외가 발생한다.")
    void whenInvalidTimeReservationThrowException() {
        //given
        //when
        //then
        assertThatThrownBy(() -> new ReservationTime(LocalTime.of(7, 0)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("예약할 수 없는 시간입니다.");
    }


}
