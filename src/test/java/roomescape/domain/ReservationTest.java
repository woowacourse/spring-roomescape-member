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

class ReservationTest {

    private static Stream<Arguments> getInvalidReservations() {
        return Stream.of(
                Arguments.of((Supplier<Reservation>) () -> new Reservation(1L, null, LocalDate.now(), new ReservationTime(1L, LocalTime.now()), new Theme(1L, "테마","테마 설명", "테마 이미지"))),
                Arguments.of((Supplier<Reservation>) () -> new Reservation(1L, "히스타", null, new ReservationTime(1L, LocalTime.now()), new Theme(1L, "테마","테마 설명", "테마 이미지"))),
                Arguments.of((Supplier<Reservation>) () -> new Reservation(1L, "히스타", LocalDate.now(), null, new Theme(1L, "테마","테마 설명", "테마 이미지"))),
                Arguments.of((Supplier<Reservation>) () -> new Reservation(1L, "히스타", LocalDate.now(), new ReservationTime(1L, LocalTime.now()), null))
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

    @DisplayName("name이 빈 값일 때(공백) IllegalArgumentException을 throw 한다.")
    @Test
    void validateBlankName() {
        // given
        Supplier<Reservation> supplier = () -> new Reservation(1L, null, LocalDate.now(), new ReservationTime(1L, LocalTime.now()), new Theme(1L, "테마","테마 설명", "테마 이미지"));


        // when & then
        assertThatIllegalArgumentException().isThrownBy(
                supplier::get
        );
    }
}
