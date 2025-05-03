package roomescape.time.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalTime;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class ReservationTimeEntityTest {
    @DisplayName("예약 시간이 운영 시간에 포함되는지 판단할 수 있다.")
    @ParameterizedTest
    @MethodSource
    void checkIsAvailable(LocalTime startAt, final boolean expected) {
        // given
        ReservationTimeEntity timeEntity = new ReservationTimeEntity(1L, startAt);

        // when
        final boolean actual = timeEntity.isAvailable();

        // then
        assertThat(actual).isSameAs(expected);
    }

    private static Stream<Arguments> checkIsAvailable() {
        return Stream.of(
                Arguments.of(LocalTime.of(9, 59), false),
                Arguments.of(LocalTime.of(10, 0), true),
                Arguments.of(LocalTime.of(22, 1), false),
                Arguments.of(LocalTime.of(22, 0), true)
        );
    }
}
