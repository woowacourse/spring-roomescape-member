package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ReservationTest {

    @Test
    @DisplayName("예약 날짜에 null 값이 입력되면 에러가 발생한다.")
    void checkNullReservationDate() {
        //given
        ReservationTime reservationTime = new ReservationTime(LocalTime.parse("20:00"));
        User user = new User("admin1@email.com", "password");
        Theme theme = new Theme("테마명", "테마 설명", "테마 이미지");

        //when & then
        assertThatThrownBy(() -> new Reservation(user, null, reservationTime, theme))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("예약 날짜가 입력되지 않았습니다.");
    }
}
