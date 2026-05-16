package roomescape.reservation.domain;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.global.exception.customException.BusinessException;
import roomescape.reservationTime.domain.ReservationTime;

class DefaultReservationPolicyTest {

    private final ReservationPolicy reservationPolicy = new DefaultReservationPolicy();

    @Test
    @DisplayName("미래 일정이면 예약 생성 시 예외가 발생하지 않는다")
    void validateCreatableDateTime_success() {
        // given
        LocalDateTime futureDateTime = LocalDateTime.now().plusMinutes(1);
        ReservationTime reservationTime = ReservationTime.create(futureDateTime.toLocalTime());

        // when & then
        assertThatCode(() -> reservationPolicy.validateCreatableDateTime(futureDateTime.toLocalDate(), reservationTime))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("지난 일정이면 예약 생성 시 예외가 발생한다")
    void validateCreatableDateTime_fail_with_past_date_time() {
        // given
        LocalDateTime pastDateTime = LocalDateTime.now().minusMinutes(1);
        ReservationTime reservationTime = ReservationTime.create(pastDateTime.toLocalTime());

        // when & then
        assertThatThrownBy(() -> reservationPolicy.validateCreatableDateTime(pastDateTime.toLocalDate(), reservationTime))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("지난 일정으로 예약할 수 없습니다.");
    }

    @Test
    @DisplayName("미래 일정이면 예약 수정 및 취소 시 예외가 발생하지 않는다")
    void validateModifiableDateTime_success() {
        // given
        LocalDateTime futureDateTime = LocalDateTime.now().plusMinutes(1);
        ReservationTime reservationTime = ReservationTime.create(futureDateTime.toLocalTime());

        // when & then
        assertThatCode(() -> reservationPolicy.validateModifiableDateTime(futureDateTime.toLocalDate(), reservationTime))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("지난 일정이면 예약 수정 및 취소 시 예외가 발생한다")
    void validateModifiableDateTime_fail_with_past_date_time() {
        // given
        LocalDateTime pastDateTime = LocalDateTime.now().minusMinutes(1);
        ReservationTime reservationTime = ReservationTime.create(pastDateTime.toLocalTime());

        // when & then
        assertThatThrownBy(() -> reservationPolicy.validateModifiableDateTime(pastDateTime.toLocalDate(), reservationTime))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("지난 일정의 예약은 수정 및 취소할 수 없습니다.");
    }
}
