package roomescape.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.exception.InvalidReservationException;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class ReservationTimeTest {

    @DisplayName("올바르지 않은 시간으로 설정하면 예외를 던진다.")
    @Test
    void invalidDateSchedule() {
        //given
        String invalidTime = "";

        //when&then
        assertThatThrownBy(() -> new ReservationTime(1, invalidTime))
                .isInstanceOf(InvalidReservationException.class)
                .hasMessage("올바르지 않은 시간입니다.");
    }

    @DisplayName("시간은 `HH:mm` 형식으로 반환한다.")
    @Test
    void getTime() {
        //given
        LocalTime time = LocalTime.now();

        //when
        ReservationTime reservationTime = new ReservationTime(1, time);

        //when&then
        assertThat(reservationTime.getStartAt()).isEqualTo(time.format(DateTimeFormatter.ofPattern("HH:mm")));
    }
}
