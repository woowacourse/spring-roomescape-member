package roomescape.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ReservationTest {

    @Test
    void 예약자명은_2글자_이상_5글자_이하만_가능하다_성공() {
        assertThatCode(() -> new Reservation(1L, "가나다", LocalDate.now(), new ReservationTime(1L, LocalTime.of(12, 0)), new Theme(1L, "name", "description", "thumbnail")))
                .doesNotThrowAnyException();
    }

    @ParameterizedTest
    @ValueSource(strings = {"가", "가나다라마바"})
    void 예약자명은_2글자_이상_5글자_이하만_가능하다_실패(String name) {
        assertThatThrownBy(() -> new Reservation(1L, name, LocalDate.now(), new ReservationTime(1L, LocalTime.of(12, 0)), new Theme(1L, "name", "description", "thumbnail")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("예약자명은 2글자에서 5글자까지만 가능합니다.");
    }
}