package roomescape.reservation.entity;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalTime;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class ReservationTimeTest {

    @DisplayName("게임 러닝 타임(2시간)을 고려하여 예약 중복 여부를 판단할 수 있다.")
    @Test
    void duplicateByRunningTime() {
        // given
        LocalTime time = LocalTime.of(10, 0);
        LocalTime duplicateTime = time.plusHours(1);
        ReservationTime reservationTime = new ReservationTime(1L, time);
        ReservationTime otherTime = new ReservationTime(2L, duplicateTime);

        // when
        final boolean isDuplicated = reservationTime.isDuplicatedWith(otherTime);

        // then
        assertThat(isDuplicated).isTrue();
    }

    @DisplayName("게임 러닝 타임(2시간) 이후의 예약은 중복이 아니다.")
    @Test
    void notDuplicateByRunningTime() {
        // given
        LocalTime time = LocalTime.of(10, 0);
        LocalTime duplicateTime = time.plusHours(2);
        ReservationTime reservationTime = new ReservationTime(1L, time);
        ReservationTime otherTime = new ReservationTime(2L, duplicateTime);

        // when
        final boolean isDuplicated = reservationTime.isDuplicatedWith(otherTime);

        // then
        assertThat(isDuplicated).isFalse();
    }

    @DisplayName("예약 시간이 운영 시간에 포함되는지 판단할 수 있다.")
    @ParameterizedTest
    @MethodSource
    void checkIsAvailable(LocalTime startAt, final boolean expected) {
        // given
        ReservationTime time = new ReservationTime(null, startAt);

        // when
        final boolean actual = time.isAvailable();

        // then
        assertThat(actual).isEqualTo(expected);
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
