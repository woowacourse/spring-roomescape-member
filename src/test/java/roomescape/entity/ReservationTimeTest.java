package roomescape.entity;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class ReservationTimeTest {

    private static final LocalTime now = LocalTime.now();

    static Stream<Arguments> ifFutureReservationTimeReturnsFalse() {
        return Stream.of(
                Arguments.of(now.truncatedTo(ChronoUnit.MINUTES)),
                Arguments.of(now.plusMinutes(1))
        );
    }

    @Test
    @DisplayName("지난 예약 시간인 경우 TRUE 반환한다.")
    void ifPastReservationTimeReturnsTrue() {
        // given
        ReservationTime reservationTime = new ReservationTime(1L, now.minusMinutes(1));

        // when
        boolean result = reservationTime.isPastTime();

        // then
        assertThat(result).isTrue();
    }

    @ParameterizedTest
    @MethodSource
    @DisplayName("미래 예약 시간인 경우 FALSE 반환한다.")
    void ifFutureReservationTimeReturnsFalse(LocalTime time) {
        // given
        ReservationTime reservationTime = new ReservationTime(1L, time);

        // when
        boolean result = reservationTime.isPastTime();

        // then
        assertThat(result).isFalse();
    }
}
