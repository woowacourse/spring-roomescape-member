package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.exception.InvalidInputException;

class ReservationTest {

    @Test
    @DisplayName("예약 날짜에 null 값이 입력되는지 확인한다.")
    void checkNullReservationDate() {
        //given
        ReservationTime reservationTime = new ReservationTime(LocalTime.parse("20:00"));
        UserName userName = new UserName("chorong");
        Theme theme = new Theme("테마명", "테마 설명", "테마 이미지");

        //when & then
        assertThatThrownBy(() -> new Reservation(userName, null, reservationTime, theme))
                .isInstanceOf(InvalidInputException.class)
                .hasMessage("예약 날짜가 입력되지 않았습니다.");
    }
}
