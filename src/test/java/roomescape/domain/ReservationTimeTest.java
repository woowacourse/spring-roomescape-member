package roomescape.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.exception.custom.InvalidInputException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ReservationTimeTest {

    @Test
    @DisplayName("유효하지 않은 값으로 생성 시, 예외를 던진다")
    void throwExceptionWhenInvalidInput() {
        //when & then
        assertThatThrownBy(() -> new ReservationTime(null))
                .isInstanceOf(InvalidInputException.class)
                .hasMessage("시간은 빈 값이 입력될 수 없습니다");
    }
}
