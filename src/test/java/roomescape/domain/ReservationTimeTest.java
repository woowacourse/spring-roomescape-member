package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class ReservationTimeTest {

    @ParameterizedTest
    @CsvSource(value = {"1,true", "0,true", "-1,false"})
    @DisplayName("예약 시간이 기준 시간보다 이전이거나 같은지 확인한다.")
    void isPastOrPresentTime(int minusMinute, boolean expected) {
        // given
        LocalTime startAt = LocalTime.now().minusMinutes(minusMinute);
        ReservationTime reservationTime = new ReservationTime(startAt);
        LocalTime limitTime = LocalTime.now();

        // when
        boolean result = reservationTime.isBeforeOrSame(limitTime);

        // then
        assertThat(result).isEqualTo(expected);
    }
}
