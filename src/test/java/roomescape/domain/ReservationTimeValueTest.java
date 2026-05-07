package roomescape.domain;

import org.junit.jupiter.api.Test;
import roomescape.exception.DomainException;
import roomescape.exception.ErrorCode;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ReservationTimeValueTest {

    @Test
    void 예약_시간_id가_null이면_도메인_예외가_발생한다() {
        assertDomainException(
                () -> new ReservationTimeId(null),
                ErrorCode.INVALID_RESERVATION_TIME_ID
        );
    }

    @Test
    void 예약_시간이_null이면_도메인_예외가_발생한다() {
        assertDomainException(
                () -> new ReservationTimeStartAt(null),
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
