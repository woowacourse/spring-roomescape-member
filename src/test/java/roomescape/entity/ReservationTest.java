package roomescape.entity;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

public class ReservationTest {

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "  ", "\t", "\n"})
    @DisplayName("이름이 null이나 공백을 입력할 경우, 예외가 발생한다.")
    void error_validationName(String name) {
        // given
        var theme = new Theme(1L, "테스트", "테스트", "테스트");
        var date = LocalDate.now();
        var time = new ReservationTime(1L, LocalTime.of(15, 0));

        // when & then
        assertThatThrownBy(() -> new Reservation(1L, name, date, time, theme))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 유효하지 않은 예약자명입니다.");
    }

    @Test
    @DisplayName("예약자의 이름이 10자인 경우 예외가 발생하지 않는다.")
    void test_validationNameLength() {
        // given
        var theme = new Theme(1L, "테스트", "테스트", "테스트");
        var name = "abcdefgfhi";
        var date = LocalDate.now();
        var time = new ReservationTime(1L, LocalTime.of(15, 0));

        // when & then
        Assertions.assertDoesNotThrow(() -> new Reservation(1L, name, date, time, theme));
    }

    @Test
    @DisplayName("예약자의 이름이 10자를 초과하는 경우 예외가 발생한다.")
    void error_validationNameLength() {
        // given
        var theme = new Theme(1L, "테스트", "테스트", "테스트");
        var name = "abcdefgfhij";
        var date = LocalDate.now();
        var time = new ReservationTime(1L, LocalTime.of(15, 0));

        // when & then
        assertThatThrownBy(() -> new Reservation(1L, name, date, time, theme))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 예약자명의 길이가 10자를 초과할 수 없습니다.");
    }

    @Test
    @DisplayName("날짜가 null일 경우, 예외가 발생한다.")
    void error_validationDate() {
        //given
        var theme = new Theme(1L, "테스트", "테스트", "테스트");
        LocalDate date = null;
        var name = "브라운";
        var time = new ReservationTime(1L, LocalTime.of(15, 0));
        //when & then
        assertThatThrownBy(() -> new Reservation(1L, name, date, time, theme))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 유효하지 않은 예약 날짜입니다.");
    }

    @Test
    @DisplayName("time 값이 null이나 공백을 입력할 경우, 예외가 발생한다.")
    void error_validationTime() {
        //given
        var theme = new Theme(1L, "테스트", "테스트", "테스트");
        ReservationTime time = null;
        var name = "브라운";
        var date = LocalDate.now();
        //when & then
        assertThatThrownBy(() -> new Reservation(1L, name, date, time, theme))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 유효하지 않은 예약 시간입니다.");
    }
}
