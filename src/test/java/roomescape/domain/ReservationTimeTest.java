package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class ReservationTimeTest {

    @ParameterizedTest
    @CsvSource(value = {"1,true", "0,true", "-1,false"})
    @DisplayName("예약 시간이 현재 시간보다 이전이거나 같은지 확인한다.")
    void isPastOrPresentTime(int minute, boolean expected) {
        // given
        LocalTime startAt = LocalTime.now().minusMinutes(minute);
        ReservationTime reservationTime = new ReservationTime(startAt);

        // when
        boolean result = reservationTime.isPastOrPresentTime();

        // then
        assertThat(result).isEqualTo(expected);
    }
}
