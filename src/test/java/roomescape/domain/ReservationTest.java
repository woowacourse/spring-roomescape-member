package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.exception.exceptions.InvalidInputException;

class ReservationTest {

    @Test
    @DisplayName("예약 날짜에 null 값이 입력되는지 확인한다.")
    void checkNullReservationDate() {
        //given
        ReservationTime reservationTime = new ReservationTime(LocalTime.parse("20:00"));
        Theme theme = new Theme("테마명", "테마 설명", "테마 이미지");
        UserName userName = new UserName("chorong");
        Email email = new Email("chorong@exampl.com");
        Password password = new Password("password2@");
        Member member = new Member(userName, email, password, Role.USER);

        //when & then
        assertThatThrownBy(() -> new Reservation(null, reservationTime, theme, member))
                .isInstanceOf(InvalidInputException.class)
                .hasMessage("예약 날짜가 입력되지 않았습니다.");
    }
}
