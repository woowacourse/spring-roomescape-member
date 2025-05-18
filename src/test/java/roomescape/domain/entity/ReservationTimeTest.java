package roomescape.domain.entity;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.time.LocalTime;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class ReservationTimeTest {

    @DisplayName("동일한 ID를 가진 경우 equals/hashCode가 동일하게 작동한다")
    @ParameterizedTest
    @MethodSource("provideTimesForEquality")
    void equalsAndHashCode(ReservationTime t1, ReservationTime t2, boolean expected) {
        assertSoftly(soft -> {
            soft.assertThat(t1.equals(t2)).isEqualTo(expected);
            if (expected) {
                soft.assertThat(t1.hashCode()).isEqualTo(t2.hashCode());
            }
        });
    }

    static Stream<Arguments> provideTimesForEquality() {
        return Stream.of(
                Arguments.of(new ReservationTime(1L, LocalTime.of(10, 0)),
                        new ReservationTime(1L, LocalTime.of(11, 0)), true),
                Arguments.of(new ReservationTime(1L, LocalTime.of(10, 0)),
                        new ReservationTime(2L, LocalTime.of(10, 0)), false),
                Arguments.of(new ReservationTime(null, LocalTime.of(10, 0)),
                        new ReservationTime(null, LocalTime.of(10, 0)), false)
        );
    }
}
