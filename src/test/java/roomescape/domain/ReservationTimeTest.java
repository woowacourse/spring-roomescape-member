package roomescape.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import roomescape.domain.exception.InvalidRequestException;
import roomescape.domain.exception.InvalidTimeException;

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

    @ParameterizedTest
    @ValueSource(strings = {"1:300", "112:33"})
    @DisplayName("유효하지 않은 시간을 입력할 경우 예외가 발생한다.")
    void validateTime(String time) {
        assertThatThrownBy(() -> ReservationTime.from(time))
                .isInstanceOf(InvalidTimeException.class);
    }

    @Test
    @DisplayName("빈 시간 요청인 경우 예외가 발생한다.")
    void validateEmpty() {
        assertThatThrownBy(() -> ReservationTime.from(""))
                .isInstanceOf(InvalidRequestException.class);
    }
}
