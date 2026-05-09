package roomescape.domain;

import org.junit.jupiter.api.Test;
import roomescape.common.exception.DomainException;
import roomescape.common.exception.ErrorCode;

import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ReservationTimeAvailabilityTest {

    private final ReservationTime reservationTime = new ReservationTime(1L, LocalTime.of(10, 0));

    @Test
    void 예약_가능_시간을_생성한다() {
        ReservationTimeAvailability availability = ReservationTimeAvailability.available(reservationTime);

        assertThat(availability.getReservationTime()).isEqualTo(reservationTime);
        assertThat(availability.isAvailable()).isTrue();
    }

    @Test
    void 예약_불가능_시간을_생성한다() {
        ReservationTimeAvailability availability = ReservationTimeAvailability.unavailable(reservationTime);

        assertThat(availability.getReservationTime()).isEqualTo(reservationTime);
        assertThat(availability.isAvailable()).isFalse();
    }

    @Test
    void 예약_가능_시간의_예약_시간이_null이면_도메인_예외가_발생한다() {
        assertDomainException(
                () -> ReservationTimeAvailability.available(null),
                ErrorCode.INVALID_RESERVATION_TIME
        );
    }

    @Test
    void 예약_불가능_시간의_예약_시간이_null이면_도메인_예외가_발생한다() {
        assertDomainException(
                () -> ReservationTimeAvailability.unavailable(null),
                ErrorCode.INVALID_RESERVATION_TIME
        );
    }

    private void assertDomainException(Runnable runnable, ErrorCode errorCode) {
        assertThatThrownBy(runnable::run)
                .isInstanceOfSatisfying(DomainException.class, exception ->
                        assertThat(exception.getErrorCode()).isEqualTo(errorCode)
                )
                .hasMessage(errorCode.message());
    }
}
