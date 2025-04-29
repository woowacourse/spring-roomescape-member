package roomescape.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ReservationTimeResponseDtoTest {

    @Test
    @DisplayName("startAt 필드의 반환 형태는 HH:mm이다.")
    void formatStartAtResponse() {
        LocalTime startAt = LocalTime.of(10, 0, 1);

        ReservationTimeResponseDto reservationTimeResponseDto = new ReservationTimeResponseDto(1L, startAt);

        assertThat(reservationTimeResponseDto.getStartAt()).isEqualTo("10:00");
    }
}
