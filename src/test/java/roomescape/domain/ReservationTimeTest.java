package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.exception.exceptions.InvalidInputException;

class ReservationTimeTest {

    @Test
    @DisplayName("예약 시간에 null 값이 입력되는지 확인한다.")
    void checkNullReservationTime() {
        //given & when & then
        assertThatThrownBy(() -> new ReservationTime(null))
                .isInstanceOf(InvalidInputException.class)
                .hasMessage("예약 시간이 입력되지 않았습니다.");
    }
}
