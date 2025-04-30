package roomescape.domain_entity;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalTime;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class ReservationTimeTest {

    static Stream<Arguments> isFutureReservationTimeReturnsFalse() {
        return Stream.of(
                Arguments.of(LocalTime.now()),
                Arguments.of(LocalTime.now().plusMinutes(1))
        );
    }

    @Test
    @DisplayName("지난 예약 시간인 경우 TRUE 반환한다.")
    void isPastReservationTimeReturnsTrue() {
        // given
        LocalTime now = LocalTime.now();
        ReservationTime reservationTime = new ReservationTime(new Id(1L), now.minusMinutes(1));

        // when
        boolean result = reservationTime.isPastTime();

        // then
        assertThat(result).isTrue();
    }

    @ParameterizedTest
    @MethodSource
    @DisplayName("미래 예약 시간인 경우 FALSE 반환한다.")
    void isFutureReservationTimeReturnsFalse(LocalTime time) {
        // given
        ReservationTime reservationTime = new ReservationTime(new Id(1L), time);

        // when
        boolean result = reservationTime.isPastTime();

        // then
        assertThat(result).isFalse();
    }
}
