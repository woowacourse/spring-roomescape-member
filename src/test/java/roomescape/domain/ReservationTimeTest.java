package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ReservationTimeTest {

    @Test
    @DisplayName("예약 시간에 null 값이 입력되면 에러가 발생한다.")
    void checkNullReservationTime() {
        //given & when & then
        assertThatThrownBy(() -> new ReservationTime(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("예약 시간이 입력되지 않았습니다.");
    }
}
