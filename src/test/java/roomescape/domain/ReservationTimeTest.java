package roomescape.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.exception.InvalidRequestException;

import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ReservationTimeTest {

    @Test
    @DisplayName("예약 시간이 null이면 도메인 예외가 발생한다.")
    void create_fail_whenStartAtIsNull() {
        assertInvalidRequestException(
                () -> new ReservationTime(null),
                "예약 시간은 비어 있을 수 없습니다."
        );
    }

    @Test
    @DisplayName("예약 시간 id가 null이면 도메인 예외가 발생한다.")
    void withId_fail_whenIdIsNull() {
        ReservationTime reservationTime = new ReservationTime(LocalTime.of(10, 0));

        assertInvalidRequestException(
                () -> reservationTime.withId(null),
                "예약 시간 id는 비어 있을 수 없습니다."
        );
    }

    @Test
    @DisplayName("이미 id가 있는 예약 시간에 id를 부여하면 도메인 예외가 발생한다.")
    void withId_fail_whenReservationTimeAlreadyHasId() {
        ReservationTime reservationTime = new ReservationTime(1L, LocalTime.of(10, 0));

        assertInvalidRequestException(
                () -> reservationTime.withId(2L),
                "이미 id가 존재하는 예약 시간입니다."
        );
    }

    private void assertInvalidRequestException(Runnable runnable, String message) {
        assertThatThrownBy(runnable::run)
                .isInstanceOf(InvalidRequestException.class)
                .hasMessage(message);
    }
}
