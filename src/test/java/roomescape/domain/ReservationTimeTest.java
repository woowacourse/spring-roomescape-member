package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("예약 시간(ReservationTime) 테스트")
class ReservationTimeTest {

    @ParameterizedTest
    @MethodSource
    @DisplayName("예약 시간이 현재 시간보다 이전이면 TRUE, 이후면 FALSE를 반환한다")
    void isPastTime(LocalTime time, boolean expected) {
        // given
        ReservationTime reservationTime = new ReservationTime(1L, time);

        // when
        boolean result = reservationTime.isPastTime();

        // then
        assertThat(result).isEqualTo(expected);
    }

    static Stream<Arguments> isPastTime() {
        LocalTime now = LocalTime.now().truncatedTo(ChronoUnit.MINUTES);
        return Stream.of(
                Arguments.of(now.minusMinutes(1), true),
                Arguments.of(now, false),
                Arguments.of(now.plusMinutes(1), false)
        );
    }
}
