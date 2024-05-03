package roomescape.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import roomescape.exception.InvalidReservationException;

class MemberNameTest {
    @DisplayName("이름은 1자 미만, 5자 초과일 경우 예외를 발생시킨다.")
    @ParameterizedTest
    @ValueSource(strings = {"", "linirini"})
    void invalidNameLength(String name) {
        //given
        ReservationDate date = new ReservationDate("2024-10-04");
        ReservationTime reservationTime = new ReservationTime(1, "10:00");
        Theme theme = new Theme("레벨2 탈출", "우테코 레벨2를 탈출하는 내용입니다.", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");

        //when&then
        assertThatThrownBy(() -> new Reservation(name, date, reservationTime, theme))
                .isInstanceOf(InvalidReservationException.class)
                .hasMessage("이름은 1자 이상, 5자 이하여야 합니다.");
    }
}
