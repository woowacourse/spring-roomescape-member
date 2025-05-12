package roomescape.unit.domain;

import static org.assertj.core.api.Assertions.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.function.Supplier;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.domain.member.Member;
import roomescape.domain.member.Role;

class ReservationTest {
    private static Theme makeTheme(Long id) {
        return new Theme(id, "테마", "테마 설명", "테마 이미지");
    }

    private static Member makeMember(Long id) {
        return new Member(id, "히스타", "hista@wtc.jjang", "1q2w3e4r!", Role.MEMBER);
    }

    private static Stream<Arguments> getInvalidReservations() {
        return Stream.of(
                // 유효하지 않는 member
                Arguments.of((Supplier<Reservation>) () ->
                        new Reservation(
                                1L,
                                null,
                                LocalDate.now(),
                                new ReservationTime(1L, LocalTime.now()),
                                makeTheme(1L))),

                // 유효하지 않는 date
                Arguments.of((Supplier<Reservation>) () ->
                        new Reservation(
                                1L,
                                makeMember(1L),
                                null,
                                new ReservationTime(1L, LocalTime.now()),
                                makeTheme(1L))),

                // 유효하지 않는 time
                Arguments.of((Supplier<Reservation>) () ->
                        new Reservation(
                                1L,
                                makeMember(1L),
                                LocalDate.now(),
                                null,
                                makeTheme(1L))),

                // 유효하지 않는 theme
                Arguments.of((Supplier<Reservation>) () ->
                        new Reservation(
                                1L,
                                makeMember(1L),
                                LocalDate.now(),
                                new ReservationTime(1L, LocalTime.now()),
                                null))
        );
    }

    @ParameterizedTest
    @MethodSource("getInvalidReservations")
    void validate(Supplier<Reservation> reservationSupplier) {
        // when & then
        assertThatIllegalArgumentException().isThrownBy(
                reservationSupplier::get
        );
    }
}
