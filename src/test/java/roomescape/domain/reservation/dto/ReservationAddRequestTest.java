package roomescape.domain.reservation.dto;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.global.exception.ClientIllegalArgumentException;

class ReservationAddRequestTest {

    @DisplayName("정상적인 값이면 ReservationAddRequest생성 시 예외가 발생하지 않는다")
    @Test
    void should_not_throw_exception_when_request_is_right() {
        LocalDate pastDate = LocalDate.now().plusDays(1);
        assertThatCode(() -> new ReservationAddRequest(pastDate, "dodo", 1L, 1L));
    }

    @DisplayName("date가 현재 날짜 보다 이전 날짜이면 ReservationAddRequest생성 시 예외가 발생한다")
    @Test
    void should_throw_ClientIllegalArgumentException_when_date_is_past() {
        LocalDate pastDate = LocalDate.now().minusDays(1);
        assertThatThrownBy(() -> new ReservationAddRequest(pastDate, "dodo", 1L, 1L))
                .isInstanceOf(ClientIllegalArgumentException.class)
                .hasMessage(pastDate + ": 예약 날짜는 현재 보다 이전일 수 없습니다");
    }
}
