package roomescape.entity;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
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
        var date = LocalDate.now();
        var time = new ReservationTime(1L, LocalTime.of(15, 0));

        // when & then
        assertThatThrownBy(() -> new Reservation(1L, name, date, time))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 유효하지 않은 이름입니다.");
    }

    @Test
    @DisplayName("날짜가 null일 경우, 예외가 발생한다.")
    void error_validationDate() {
        //given
        LocalDate date = null;
        var name = "브라운";
        var time = new ReservationTime(1L, LocalTime.of(15, 0));
        //when & then
        assertThatThrownBy(() -> new Reservation(1L, name, date, time))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 유효하지 않은 날짜입니다.");
    }

    @Test
    @DisplayName("time 값이 null이나 공백을 입력할 경우, 예외가 발생한다.")
    void error_validationTime() {
        //given
        ReservationTime time = null;
        var name = "브라운";
        var date = LocalDate.now();
        //when & then
        assertThatThrownBy(() -> new Reservation(1L, name, date, time))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 유효하지 않은 시간입니다.");
    }
}
