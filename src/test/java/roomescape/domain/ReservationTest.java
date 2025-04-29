package roomescape.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.stream.Stream;

class ReservationTest {

    @DisplayName("Date 이 존재하지 않으면 생성 불가능하다")
    @Test
    void invalidReservationDateTimeTest() {
        Assertions.assertThatThrownBy(() ->
                        new Reservation(1L, "가이온", null, new ReservationTime(1L, LocalTime.now())))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("ReservationTime이 존재하지 않으면 생성 불가능하다")
    @Test
    void invalidReservationTimeTest() {
        Assertions.assertThatThrownBy(() ->
                        new Reservation(1L, "가이온", LocalDate.now(), null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("공백이거나 이름이 존재하지 않는 경우 생성할 수 없다.")
    @ParameterizedTest
    @MethodSource("invalidNames")
    void invalidReservationNameTest(String reservationName) {
        Long id = 1L;
        LocalDate date = LocalDate.now();
        ReservationTime reservationTime = new ReservationTime(1L, LocalTime.now());

        Assertions.assertThatThrownBy(() -> new Reservation(id, reservationName, date, reservationTime))
                .isInstanceOf(IllegalArgumentException.class);
    }

    static Stream<Arguments> invalidNames() {
        return Stream.of(
                Arguments.of(" "),
                Arguments.of(""),
                Arguments.of((String) null)
        );
    }
}
