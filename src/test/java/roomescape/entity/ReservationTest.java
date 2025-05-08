package roomescape.entity;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class ReservationTest {

    @DisplayName("유효한 값을 입력할 경우, 성공적으로 ReservationTime을 생성한다.")
    void buildReservation() {
        // given
        String name = "테스트";
        Theme theme = new Theme(1L, "테스트", "테스트", "테스트");
        LocalDate date = LocalDate.now();
        ReservationTime time = new ReservationTime(1L, LocalTime.of(15, 0));
        // when & then
        assertThatCode(() -> Reservation.create(1L, name, date, time, theme)).doesNotThrowAnyException();
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "  ", "\t", "\n"})
    @DisplayName("이름이 null이나 공백을 입력할 경우, 예외가 발생한다.")
    void validationName(String name) {
        // given
        Theme theme = new Theme(1L, "테스트", "테스트", "테스트");
        LocalDate date = LocalDate.now();
        ReservationTime time = new ReservationTime(1L, LocalTime.of(15, 0));

        // when & then
        assertThatThrownBy(() -> Reservation.create(1L, name, date, time, theme))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 유효하지 않은 예약자명입니다.");
    }

    @Test
    @DisplayName("예약자의 이름이 10자를 초과하는 경우 예외가 발생한다.")
    void validationNameLength() {
        // given
        Theme theme = new Theme(1L, "테스트", "테스트", "테스트");
        String name = "abcdefgfhij";
        LocalDate date = LocalDate.now();
        ReservationTime time = new ReservationTime(1L, LocalTime.of(15, 0));

        // when & then
        assertThatThrownBy(() -> Reservation.create(1L, name, date, time, theme))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 예약자명의 길이가 10자를 초과할 수 없습니다.");
    }

    @Test
    @DisplayName("날짜가 null일 경우, 예외가 발생한다.")
    void validationDate() {
        //given
        Theme theme = new Theme(1L, "테스트", "테스트", "테스트");
        LocalDate date = null;
        String name = "브라운";
        ReservationTime time = new ReservationTime(1L, LocalTime.of(15, 0));
        //when & then
        assertThatThrownBy(() -> Reservation.create(1L, name, date, time, theme))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 유효하지 않은 예약 날짜입니다.");
    }

    @Test
    @DisplayName("time 값이 null이나 공백을 입력할 경우, 예외가 발생한다.")
    void validationTime() {
        //given
        Theme theme = new Theme(1L, "테스트", "테스트", "테스트");
        ReservationTime time = null;
        String name = "브라운";
        LocalDate date = LocalDate.now();
        //when & then
        assertThatThrownBy(() -> Reservation.create(1L, name, date, time, theme))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 유효하지 않은 예약 시간입니다.");
    }

    @Test
    @DisplayName("DateTime 값이 과거일 경우, 예외가 발생한다.")
    void createIfDateTimeValid() {
        //given
        LocalDate date = LocalDate.MIN;
        String name = "브라운";
        ReservationTime time = new ReservationTime(1L, LocalTime.MIN);
        Theme theme = new Theme(1L, "테스트", "테스트", "테스트");
        //when & then
        assertThatThrownBy(() -> Reservation.createIfDateTimeValid(name, date, time, theme))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("[ERROR] 예약이 불가능한 시간입니다: ");
    }

}
