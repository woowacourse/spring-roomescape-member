package roomescape.time.domain;

import org.junit.jupiter.api.Test;
import roomescape.global.exception.BusinessException;
import roomescape.global.exception.ErrorCode;

import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ReservationTimeTest {

    @Test
    void 유효한_시작_시간으로_예약_시간을_생성하면_필드가_저장된다() {
        ReservationTime time = new ReservationTime(1L, LocalTime.of(10, 0));

        assertThat(time.getStartAt()).isEqualTo(LocalTime.of(10, 0));
    }

    @Test
    void 시작_시간이_null이면_예외가_발생한다() {
        assertThatThrownBy(() -> new ReservationTime(1L, null))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.INVALID_INPUT);
    }

}
