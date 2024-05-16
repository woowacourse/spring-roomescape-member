package roomescape.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import roomescape.exceptions.InvalidRequestBodyFieldException;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ReservationTest {

    private final Member member = new Member("잉크", "asdf@a.com", "1234", Role.ADMIN);

    @Test
    @DisplayName("올바른 입력시 객체를 생성한다.")
    void validReservation() {
        assertThat(new Reservation(member, LocalDate.now(), new ReservationTime(LocalTime.now()), new Theme("레벨2 탈출", "우테코 레벨2를 탈출하는 내용입니다.", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"))).isNotNull();
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("멤버에 대한 입력이 올바르지 않으면 예외가 발생한다.")
    void invalidNameReservation(Member member) {
        assertThatThrownBy(() -> new Reservation(member, LocalDate.now(), new ReservationTime(LocalTime.now()), new Theme("레벨2 탈출", "우테코 레벨2를 탈출하는 내용입니다.", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg")))
                .isInstanceOf(InvalidRequestBodyFieldException.class);
    }

    @Test
    @DisplayName("날짜에 대한 입력이 올바르지 않으면 예외가 발생한다.")
    void invalidDate() {
        assertThatThrownBy(() -> new Reservation(member, null, new ReservationTime(LocalTime.now()), new Theme("레벨2 탈출", "우테코 레벨2를 탈출하는 내용입니다.", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg")))
                .isInstanceOf(InvalidRequestBodyFieldException.class);
    }

    @Test
    @DisplayName("예약시간이 올바르지 않으면 예외가 발생한다.")
    void invalidTime() {
        assertThatThrownBy(() -> new Reservation(member, LocalDate.now(), null, new Theme("레벨2 탈출", "우테코 레벨2를 탈출하는 내용입니다.", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg")))
                .isInstanceOf(InvalidRequestBodyFieldException.class);
    }
}
