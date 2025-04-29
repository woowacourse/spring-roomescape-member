package roomescape.time.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import roomescape.time.entity.ReservationTimeEntity;

import java.time.LocalTime;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class ReservationTimeEntityTest {
    @DisplayName("게임 러닝 타임(2시간)을 고려하여 예약 중복 여부를 판단할 수 있다.")
    @Test
    void duplicateByRunningTime() {
        // given
        LocalTime time = LocalTime.of(10, 0);
        LocalTime duplicateTime = time.plusHours(1);
        ReservationTimeEntity timeEntity = new ReservationTimeEntity(1L, time);
        ReservationTimeEntity otherTimeEntity = new ReservationTimeEntity(2L, duplicateTime);

        // when
        final boolean isDuplicated = timeEntity.isDuplicatedWith(otherTimeEntity);

        // then
        assertThat(isDuplicated).isTrue();
    }

    @DisplayName("게임 러닝 타임(2시간) 이후의 예약은 중복이 아니다.")
    @Test
    void notDuplicateByRunningTime() {
        // given
        LocalTime time = LocalTime.of(10, 0);
        LocalTime duplicateTime = time.plusHours(2);
        ReservationTimeEntity timeEntity = new ReservationTimeEntity(1L, time);
        ReservationTimeEntity otherTimeEntity = new ReservationTimeEntity(2L, duplicateTime);

        // when
        final boolean isDuplicated = timeEntity.isDuplicatedWith(otherTimeEntity);

        // then
        assertThat(isDuplicated).isFalse();
    }

    @DisplayName("예약 시간이 운영 시간에 포함되는지 판단할 수 있다.")
    @ParameterizedTest
    @MethodSource
    void checkIsAvailable(LocalTime startAt, final boolean expected) {
        // given
        ReservationTimeEntity timeEntity = new ReservationTimeEntity(null, startAt);

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
