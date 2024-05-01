package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ReservationTest {
    @DisplayName("정상 생성 테스트")
    @Test
    void validCreate() {
        assertDoesNotThrow(() -> new Reservation(1L, "aa", "9999-12-31",
                new ReservationTime("10:00")));
    }

    @DisplayName("현재 시간보다 이전의 시간을 입력하면 예외를 발생시킨다.")
    @Test
    void validateDateAndTime() { //TODO 현재 시간 입력받게 변경?
        assertThatThrownBy(() -> new Reservation(1L, "브라운", "2024-05-01",
                new ReservationTime("10:00")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("예약할 수 없는 날짜입니다.");
    }
}
