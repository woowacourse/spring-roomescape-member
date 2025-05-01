package roomescape.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ReservationTimeResponseTest {

    @Test
    @DisplayName("startAt 필드의 반환 형태는 HH:mm이다.")
    void formatStartAtResponse() {
        LocalTime startAt = LocalTime.of(10, 0, 1);

        ReservationTimeResponse reservationTimeResponse = new ReservationTimeResponse(1L, startAt);

        assertThat(reservationTimeResponse.getStartAt()).isEqualTo("10:00");
    }
}
