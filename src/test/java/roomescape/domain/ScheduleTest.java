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
        Theme theme = new Theme("레벨2 탈출", "우테코 레벨2를 탈출하는 내용입니다.",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");

        //when&then
        assertThatThrownBy(() -> new Reservation(name, dateBeforeCurrent, reservationTime, theme))
                .isInstanceOf(InvalidReservationException.class)
                .hasMessage("현재보다 이전으로 일정을 설정할 수 없습니다.");
    }
}
