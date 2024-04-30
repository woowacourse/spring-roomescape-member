package roomescape.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ReservationTimeTest {

    @Test
    @DisplayName("예약 시간에 아이디를 부여한다.")
    void assignId() {
        // given
        ReservationTime time = new ReservationTime(null, LocalTime.of(10, 0));
        ReservationTime expected = new ReservationTime(2L, LocalTime.of(10, 0));

        // when
        ReservationTime actual = time.assignId(2L);

        // then
        assertThat(actual).isEqualTo(expected);
    }
}
