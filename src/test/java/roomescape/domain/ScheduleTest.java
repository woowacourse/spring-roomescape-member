package roomescape.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.exception.InvalidReservationException;

class ScheduleTest {
    @DisplayName("일정은 현재 이전으로 설정하면 예외를 던진다.")
    @Test
    void invalidTermSchedule() {
        //given
        String name = "lini";
        String dateBeforeCurrent = "2023-10-04";
        String time = "10:00";
        ReservationTime reservationTime = new ReservationTime(1, time);

        //when&then
        assertThatThrownBy(() -> new Reservation(name, dateBeforeCurrent, reservationTime))
                .isInstanceOf(InvalidReservationException.class)
                .hasMessage("현재보다 이전으로 일정을 설정할 수 없습니다.");
    }
}
