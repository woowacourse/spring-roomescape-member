package roomescape.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ReservationTest {

    @DisplayName("날짜가 형식에 맞지 않을 때 예외를 던진다.")
    @Test
    void validateDateTest_whenDateFormatIsNotMatch() {
        String date = "20-20-20";
        ReservationTime time = new ReservationTime(null, "9:00");

        assertThatThrownBy(() -> new Reservation(null, "커찬", date, time))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("날짜(%s)가 yyyy-MM-dd에 맞지 않습니다.".formatted(date));
    }
}
