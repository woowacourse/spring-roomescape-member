package roomescape.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalTime;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ReservationTimeTest {

    @ParameterizedTest
    @MethodSource(value = "invalidTimes")
    @DisplayName("예약 시간은 null이 아니며, 10분 단위이다.")
    void validate(LocalTime invalidTime) {
        // when & then
        assertThatThrownBy(() -> new ReservationTime(invalidTime))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private static Stream<LocalTime> invalidTimes() {
        return Stream.of(null, LocalTime.of(1, 23));
    }
}
