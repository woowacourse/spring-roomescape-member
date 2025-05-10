package roomescape.domain;

import static org.assertj.core.api.Assertions.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.function.Supplier;
import java.util.stream.Stream;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import roomescape.common.exception.ReservationValidationException;

class ReservationTest {

    @ParameterizedTest
    @MethodSource("getInvalidReservations")
    void validate(Supplier<Reservation> reservationSupplier) {
        // when & then
        assertThatThrownBy(reservationSupplier::get).isInstanceOf(ReservationValidationException.class);
    }

    private static Stream<Arguments> getInvalidReservations() {
        return Stream.of(
                Arguments.of((Supplier<Reservation>) () -> new Reservation(1L, null, new ReservationTime(1L, LocalTime.now()), new Theme(1L, "테마","테마 설명", "테마 이미지"),new Member(1L,"이름","test@test.com",MemberRole.USER,"1234"))),
                Arguments.of((Supplier<Reservation>) () -> new Reservation(1L, LocalDate.now(), null, new Theme(1L, "테마","테마 설명", "테마 이미지"),new Member(1L,"이름","test@test.com",MemberRole.USER,"1234"))),
                Arguments.of((Supplier<Reservation>) () -> new Reservation(1L, LocalDate.now(), new ReservationTime(1L, LocalTime.now()), null,new Member(1L,"이름","test@test.com",MemberRole.USER,"1234"))),
                Arguments.of((Supplier<Reservation>) () -> new Reservation(1L, LocalDate.now(), new ReservationTime(1L, LocalTime.now()), new Theme(1L, "테마","테마 설명", "테마 이미지"),null))
        );
    }
}
