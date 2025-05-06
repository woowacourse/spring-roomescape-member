package roomescape.application.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.domain.ReservationTime;

class ReservationTimeResponseTest {

    @Test
    @DisplayName("startAt 필드의 반환 형태는 HH:mm이다.")
    void formatStartAtResponse() {
        // given
        LocalTime startAt = LocalTime.of(10, 0, 1);
        ReservationTime reservationTime = new ReservationTime(1L, startAt);

        ReservationTimeResponse reservationTimeResponse = new ReservationTimeResponse(reservationTime);

        // when
        String result = reservationTimeResponse.startAt();

        // then\
        assertThat(result).isEqualTo("10:00");
    }
}
