package roomescape.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import roomescape.Fixtures;

public class ReservationTest {

    @ParameterizedTest
    @MethodSource("nullValues")
    @DisplayName("예약 생성 시 id가 아닌 모든 값들이 존재하지 않으면 예외가 발생한다")
    void anyValueNullException(String name, LocalDate date, TimeSlot timeSlot) {
        // given & when & then
        assertThatThrownBy(() -> Reservation.register(1L, name, date, timeSlot))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("이름이 여섯 글자 이상이면 예외가 발생한다")
    void nameLengthException() {
        // given & when & then
        assertThatThrownBy(() -> Reservation.register(
            1L,
            "여섯글자이름",
            LocalDate.of(2023, 12, 1),
            new TimeSlot(1L, LocalTime.of(10, 0)))
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("예약 일시가 주어진 일시보다 이전인지 확인한다")
    void isBefore() {
        // given
        var reserveDate = LocalDate.of(2023, 12, 1);
        var reserveTime = LocalTime.of(10, 0);
        var compareDateTime = LocalDateTime.of(reserveDate, reserveTime).plusDays(1);
        var reservation = Reservation.register(1L, "리버", reserveDate, new TimeSlot(1L, reserveTime));

        // when
        boolean isBefore = reservation.isBefore(compareDateTime);

        // then
        assertThat(isBefore).isTrue();
    }

    @Test
    @DisplayName("예약 일시가 주어진 예약과 같은 일시인지 확인한다")
    void isSameDateTime() {
        // given
        var reserveDate = LocalDate.of(2023, 12, 1);
        var reserveTime = LocalTime.of(10, 0);
        var reservation = Reservation.register(1L, "리버", reserveDate, new TimeSlot(1L, reserveTime));
        var otherReservation = Reservation.register(2L, "포포", reserveDate, new TimeSlot(1L, reserveTime));

        // when
        boolean isSameDateTime = reservation.isSameDateTime(otherReservation);

        // then
        assertThat(isSameDateTime).isTrue();
    }

    private static Stream<Arguments> nullValues() {
        return Stream.of(
            Arguments.of(
                null,
                LocalDate.of(2023, 12, 1),
                new TimeSlot(1L, LocalTime.of(10, 0))
            ),
            Arguments.of(
                "brown",
                null,
                new TimeSlot(1L, LocalTime.of(10, 0))
            ),
            Arguments.of(
                "brown",
                LocalDate.of(2023, 12, 1),
                null
            )
        );
    }
}
