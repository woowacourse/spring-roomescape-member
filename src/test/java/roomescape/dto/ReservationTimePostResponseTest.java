package roomescape.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.dto.response.ReservationTimePostResponse;

class ReservationTimePostResponseTest {

    @Test
    @DisplayName("startAt 필드의 반환 형태는 HH:mm이다.")
    void formatStartAtResponse() {
        LocalTime startAt = LocalTime.of(10, 0, 1);

        ReservationTimePostResponse reservationTimePostResponse = new ReservationTimePostResponse(1L, startAt);

        assertThat(reservationTimePostResponse.getStartAt()).isEqualTo("10:00");
    }
}
