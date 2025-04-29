package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class ReservationTimeTest {

    @DisplayName("아이디가 같으면 true를, 다르면 false를 반환한다.")
    @ParameterizedTest
    @CsvSource(value = "1, 1, true, 1, 2, false")
    void equalIdTest(final long firstId, final long secondId, boolean result) {

        // given
        ReservationTime reservationTime = new ReservationTime(firstId, LocalTime.of(10, 0));

        // when & then
        assertThat(reservationTime.isEqualId(secondId)).isEqualTo(result);
    }
}
