package roomescape.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class ReservationTest {

    @ParameterizedTest
    @ValueSource(strings = {"", " "})
    void nameBlankExceptionTest(String name) {
        ReservationTime reservationTime = new ReservationTime(1L, LocalTime.of(10, 0));
        Theme theme = new Theme("피즈의 모험", "모험 이야기", "url.jpg");
        assertThatThrownBy(() -> new Reservation(1L, name, LocalDate.of(2026, 5, 2), reservationTime, theme))
                .hasMessage("[ERROR] 이름은 비어 있을 수 없습니다.")
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void dateNullExceptionTest() {
        ReservationTime reservationTime = new ReservationTime(1L, LocalTime.of(10, 0));
        Theme theme = new Theme("피즈의 모험", "모험 이야기", "url.jpg");
        assertThatThrownBy(() -> new Reservation(1L, "fizz", null, reservationTime, theme))
                .hasMessage("[ERROR] 날짜는 비어 있을 수 없습니다.")
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void reservationTimeNullExceptionTest() {
        Theme theme = new Theme("피즈의 모험", "모험 이야기", "url.jpg");
        assertThatThrownBy(() -> new Reservation(1L, "fizz", LocalDate.of(2026, 5, 2), null, theme))
                .hasMessage("[ERROR] 예약 시간은 비어 있을 수 없습니다.")
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void themeNullExceptionTest() {
        ReservationTime reservationTime = new ReservationTime(1L, LocalTime.of(10, 0));
        assertThatThrownBy(() -> new Reservation(1L, "fizz", LocalDate.of(2026, 5, 2), reservationTime, null))
                .hasMessage("[ERROR] 테마는 비어 있을 수 없습니다.")
                .isInstanceOf(IllegalArgumentException.class);
    }
}