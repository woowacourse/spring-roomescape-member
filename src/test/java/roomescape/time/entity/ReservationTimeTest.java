package roomescape.time.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import roomescape.exception.BadRequestException;

import java.time.LocalTime;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

class ReservationTimeTest {
    @DisplayName("운영 시간 이외에는 새로운 예약 시간을 생성할 수 없다.")
    @ParameterizedTest
    @MethodSource
    void checkIsAvailable(LocalTime startAt) {
        // given

        // when & then
        assertThatThrownBy(() -> {
            ReservationTime.create(startAt);
        }).isInstanceOf(BadRequestException.class);
    }

    private static Stream<Arguments> checkIsAvailable() {
        return Stream.of(
                Arguments.of(LocalTime.of(9, 59)),
                Arguments.of(LocalTime.of(22, 1))
        );
    }

    @DisplayName("운영 시간에는 새로운 예약 시간을 생성할 수 있다.")
    @ParameterizedTest
    @MethodSource
    void checkIsNotAvailable(LocalTime startAt) {
        // given

        // when & then
        assertThatCode(() -> ReservationTime.create(startAt)).doesNotThrowAnyException();
    }

    private static Stream<Arguments> checkIsNotAvailable() {
        return Stream.of(
                Arguments.of(LocalTime.of(12, 0)),
                Arguments.of(LocalTime.of(10, 0)),
                Arguments.of(LocalTime.of(22, 0))
        );
    }

    @DisplayName("운영 시간 이외의 시간을 of 메서드를 통해 불러올 수 있다. - 운영시간 변경 고려")
    @Test
    void loadPastReservationTime() {
        // given
        LocalTime startAt = LocalTime.of(8, 0);

        // when & then
        assertThatCode(() -> ReservationTime.of(1L, startAt)).doesNotThrowAnyException();
    }
}
