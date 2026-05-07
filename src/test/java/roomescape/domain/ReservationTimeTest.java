package roomescape.domain;

import org.junit.jupiter.api.Test;
import roomescape.exception.InvalidRequestException;

import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ReservationTimeTest {

    @Test
    void 예약_시간이_null이면_도메인_예외가_발생한다() {
        assertInvalidRequestException(
                () -> new ReservationTime(null),
                "예약 시간은 비어 있을 수 없습니다."
        );
    }

    @Test
    void 예약_시간_id가_null이면_도메인_예외가_발생한다() {
        ReservationTime reservationTime = new ReservationTime(LocalTime.of(10, 0));

        assertInvalidRequestException(
                () -> reservationTime.withId(null),
                "예약 시간 id는 비어 있을 수 없습니다."
        );
    }

    @Test
    void 이미_id가_있는_예약_시간에_id를_부여하면_도메인_예외가_발생한다() {
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
