package roomescape.reservationTime.dto;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.reservation.dto.ReservationRequest;

class ReservationTimeRequestTest {

    @DisplayName("시간이 null일 경우 예외가 발생한다.")
    @Test
    void test1() {
        assertThatThrownBy(() -> new ReservationTimeRequest(null))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
