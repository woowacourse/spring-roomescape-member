package roomescape.reservationTime.dto.user;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.common.exception.InvalidDateException;
import roomescape.common.exception.InvalidIdException;
import roomescape.reservation.dto.ReservationRequest;

class AvailableReservationTimeRequestTest {

    @DisplayName("날짜가 널 값일 경우 예외가 발생한다.")
    @Test
    void exception_date_null() {
        assertThatThrownBy(() -> new ReservationRequest("riwon", null, 1L, 1L))
                .isInstanceOf(InvalidDateException.class);
    }

    @DisplayName("날짜가 현재보다 이전일 경우 예외가 발생한다.")
    @Test
    void exception_date_before() {
        assertThatThrownBy(() -> new ReservationRequest("riwon", LocalDate.now().minusDays(1), 1L, 1L))
                .isInstanceOf(InvalidDateException.class);
    }

    @DisplayName("테마 아이디가 널 값인 경우 예외가 발생한다.")
    @Test
    void exception_themeId_null() {
        assertThatThrownBy(() -> new ReservationRequest("riwon", LocalDate.now(), 1L, null))
                .isInstanceOf(InvalidIdException.class);
    }
}
