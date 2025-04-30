package roomescape.service.reservation;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class ReservationTest {

    @DisplayName("예약자명은 최소 1글자, 최대 5글자가 아니면 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {"aaaaaa", " ", "   "})
    @NullAndEmptySource
    void testValidateName(String name) {
        // given
        Long id = 1L;
        LocalDate date = LocalDate.now();
        ReservationTime time = new ReservationTime(1L, LocalTime.now());
        Theme theme = new Theme(1L, "우테코방탈출", "탈출탈출탈출", "abcdefg");
        // when
        // then
        assertThatThrownBy(() -> new Reservation(id, name, date, time, theme))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("예약자명은 최소 1글자, 최대 5글자여야합니다.");
    }
}
