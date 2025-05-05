package roomescape.reservation.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import roomescape.reservationTime.domain.ReservationTime;
import roomescape.theme.domain.Theme;

class ReservationTest {

    @ParameterizedTest
    @DisplayName("같은 시간 확인 테스트")
    @CsvSource({"20:10,true", "20:20,false", "21:10,false", "19:09,false"})
    void isSameTime_Test(LocalTime localTime, boolean expected) {
        ReservationTime reservationTime1 = ReservationTime.createWithId(1L, LocalTime.of(20, 10));
        Theme theme = Theme.createWithoutId("a", "a", "a");
        theme = theme.assignId(1L);
        Reservation reservation = Reservation.createWithId(1L, "a", LocalDate.of(2000, 11, 2), reservationTime1, theme);

        ReservationTime reservationTime2 = ReservationTime.createWithId(2L, localTime);

        assertThat(reservation.isSameTime(reservationTime2)).isEqualTo(expected);
    }

    @Test
    @DisplayName("Id 할당 테스트")
    void assignId_Test() {
        ReservationTime reservationTime1 = ReservationTime.createWithId(1L, LocalTime.of(20, 10));
        Theme theme = Theme.createWithId(1L, "a", "a", "a");
        Reservation withoutId = Reservation.createWithoutId(LocalDateTime.of(1999,11,2,20,10),"a", LocalDate.of(2000, 11, 2), reservationTime1, theme);

        Reservation reservation = withoutId.assignId(1L);

        assertThat(reservation.getId()).isEqualTo(1L);
        assertThat(reservation.getName()).isEqualTo("a");
    }

    @ParameterizedTest
    @DisplayName("과거 시간 예약 검증 테스트")
    @MethodSource
    void past_time_reservation_validate_test(LocalDate date, LocalTime time) {
        LocalDateTime now = LocalDateTime.of(2025, 5, 5, 10, 0);
        ReservationTime reservationTime = ReservationTime.createWithId(1L, time);
        Theme theme = Theme.createWithId(1L, "a", "a", "a");

        assertThatThrownBy(() -> Reservation.createWithoutId(now, "a", date, reservationTime, theme))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private static Stream<Arguments> past_time_reservation_validate_test() {
        return Stream.of(
                Arguments.of(LocalDate.of(2024, 5, 5), LocalTime.of(10, 0)),
                Arguments.of(LocalDate.of(2025, 4, 5), LocalTime.of(10, 0)),
                Arguments.of(LocalDate.of(2025, 5, 4), LocalTime.of(10, 0)),
                Arguments.of(LocalDate.of(2025, 5, 5), LocalTime.of(9, 0))
        );
    }

}