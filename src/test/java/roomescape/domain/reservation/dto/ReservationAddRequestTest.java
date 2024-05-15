package roomescape.domain.reservation.dto;

import static org.assertj.core.api.Assertions.assertThatCode;
import static roomescape.fixture.LocalDateFixture.AFTER_ONE_DAYS_DATE;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ReservationAddRequestTest {

    @DisplayName("정상적인 값이면 ReservationAddRequest생성 시 예외가 발생하지 않는다")
    @Test
    void should_not_throw_exception_when_request_is_right() {
        assertThatCode(() -> new ReservationAddRequest(AFTER_ONE_DAYS_DATE, 1L, 1L, 1L));
    }
}
