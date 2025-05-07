package roomescape.application.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import roomescape.domain.ReservationTime;

class ReservationTimeResponseTest {

    @ParameterizedTest
    @DisplayName("startAt 필드의 반환 형태는 HH:mm이다.")
    @CsvSource({
            "10, 0, 1, 10:00",
            "15, 30, 0, 15:30",
            "23, 59, 59, 23:59",
            "0, 0, 0, 00:00"
    })
    void formatStartAtResponse(int hour, int minute, int second, String expected) {
        // given
        LocalTime startAt = LocalTime.of(hour, minute, second);
        ReservationTime reservationTime = new ReservationTime(1L, startAt);

        ReservationTimeResponse reservationTimeResponse = new ReservationTimeResponse(reservationTime);

        // when
        String result = reservationTimeResponse.startAt();

        // then
        assertThat(result).isEqualTo(expected);
    }
}
