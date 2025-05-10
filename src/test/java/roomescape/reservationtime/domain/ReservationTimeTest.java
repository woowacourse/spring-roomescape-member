package roomescape.reservationtime.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.exception.custom.InvalidInputException;

class ReservationTimeTest {

    @DisplayName("예약 시간 생성시, 예약 시간이 빈 값이면 예외를 던진다")
    @Test
    void createReservationTimeTest_WhenTimeIsNull() {
        // given // when // then
        assertThatThrownBy(() -> new ReservationTime(null))
                .isInstanceOf(InvalidInputException.class)
                .hasMessage("시간은 빈 값이 입력될 수 없습니다");
    }

    @DisplayName("해당 예약 시간이 현재 시간보다 과거인 경우 true를 반환한다")
    @Test
    void isPastTimeTest_WhenTimeIsPast() {
        // given
        final LocalTime pastTime = LocalTime.now().minusMinutes(1);
        final ReservationTime reservationTime = new ReservationTime(pastTime);

        // when // then
        assertThat(reservationTime.isPastTime()).isTrue();
    }

    @DisplayName("해당 예약 시간이 현재 시간보다 미래인 경우 false를 반환한다")
    @Test
    void isPastTimeTest_WhenTimeIsFuture() {
        // given
        final LocalTime futureTime = LocalTime.now().plusMinutes(1);
        final ReservationTime reservationTime = new ReservationTime(futureTime);

        // when // then
        assertThat(reservationTime.isPastTime()).isFalse();
    }
}
