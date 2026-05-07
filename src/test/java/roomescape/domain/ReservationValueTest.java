package roomescape.domain;

import org.junit.jupiter.api.Test;
import roomescape.exception.DomainException;
import roomescape.exception.ErrorCode;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ReservationValueTest {

    @Test
    void 예약_id가_null이면_도메인_예외가_발생한다() {
        assertDomainException(
                () -> new ReservationId(null),
                ErrorCode.INVALID_RESERVATION_ID
        );
    }

    @Test
    void 예약자_이름이_null이면_도메인_예외가_발생한다() {
        assertDomainException(
                () -> new ReservationName(null),
                ErrorCode.INVALID_RESERVATION_NAME
        );
    }

    @Test
    void 예약자_이름이_비어있으면_도메인_예외가_발생한다() {
        assertDomainException(
                () -> new ReservationName(" "),
                ErrorCode.INVALID_RESERVATION_NAME
        );
    }

    @Test
    void 예약_날짜가_null이면_도메인_예외가_발생한다() {
        assertDomainException(
                () -> new ReservationDate(null),
                ErrorCode.INVALID_RESERVATION_DATE
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
