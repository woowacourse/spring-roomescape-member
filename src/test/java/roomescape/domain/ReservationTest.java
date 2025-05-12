package roomescape.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import roomescape.domain.member.Member;
import roomescape.domain.member.Role;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.stream.Stream;

class ReservationTest {

    static Stream<Arguments> invalidNames() {
        return Stream.of(
                Arguments.of(" "),
                Arguments.of(""),
                Arguments.of((String) null)
        );
    }

    @DisplayName("Date 이 존재하지 않으면 생성 불가능하다")
    @Test
    void invalidReservationDateTimeTest() {
        Member member = new Member(1L, "가이온", "hello@woowa.com", Role.USER, "password");
        Assertions.assertThatThrownBy(() ->
                        new Reservation(1L, member, null, new ReservationTime(1L, LocalTime.now()), new Theme(1L, "우테코", "방탈출", ".png")))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("ReservationTime이 존재하지 않으면 생성 불가능하다")
    @Test
    void invalidReservationTimeTest() {
        Member member = new Member(1L, "가이온", "hello@woowa.com", Role.USER, "password");
        Assertions.assertThatThrownBy(() ->
                        new Reservation(1L, member, LocalDate.now(), null, new Theme(1L, "우테코", "방탈출", ".png")))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("공백이거나 이름이 존재하지 않는 경우 생성할 수 없다.")
    @ParameterizedTest
    @MethodSource("invalidNames")
    void invalidReservationNameTest(String reservationName) {
        Long id = 1L;
        LocalDate date = LocalDate.now();
        Member member = new Member(1L, "가이온", "hello@woowa.com", Role.USER, "password");
        ReservationTime reservationTime = new ReservationTime(1L, LocalTime.now());
        Theme theme = new Theme(1L, "우테코", "방탈출", ".png");

        Assertions.assertThatThrownBy(() -> new Reservation(id, member, date, reservationTime, theme))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
