package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ReservationTest {

    @Test
    @DisplayName("예약 날짜에 null 값이 입력되는지 확인한다.")
    void checkNullReservationDate() {
        //given
        ReservationTime reservationTime = new ReservationTime(LocalTime.parse("20:00"));
        UserName userName = new UserName("chorong");

        //when & then
        assertThatThrownBy(() -> new Reservation(userName, null, reservationTime))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("예약 날짜가 입력되지 않았습니다.");
    }
}
