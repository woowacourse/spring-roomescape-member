package roomescape.entity;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ReservationTest {

    @DisplayName("유효한 값을 입력할 경우, 성공적으로 ReservationTime을 생성한다.")
    void buildReservation() {
        // given
        Theme theme = new Theme(1L, "테스트", "테스트", "테스트");
        LocalDate date = LocalDate.now();
        ReservationTime time = new ReservationTime(1L, LocalTime.of(15, 0));
        Member member = new Member(1L, "테스트", "test@example.com", "테스트");
        // when & then
        assertThatCode(() -> Reservation.create(1L, date, time, theme, member)).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("날짜가 null일 경우, 예외가 발생한다.")
    void validationDate() {
        //given
        Theme theme = new Theme(1L, "테스트", "테스트", "테스트");
        LocalDate date = null;
        ReservationTime time = new ReservationTime(1L, LocalTime.of(15, 0));
        Member member = new Member(1L, "테스트", "test@example.com", "테스트");
        //when & then
        assertThatThrownBy(() -> Reservation.create(1L, date, time, theme, member))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 유효하지 않은 예약 날짜입니다.");
    }

    @Test
    @DisplayName("member 값이 null이나 공백을 입력할 경우, 예외가 발생한다.")
    void validationMember() {
        //given
        Theme theme = new Theme(1L, "테스트", "테스트", "테스트");
        ReservationTime time = new ReservationTime(1L, LocalTime.of(15, 0));
        LocalDate date = LocalDate.now();
        Member member = null;
        //when & then
        assertThatThrownBy(() -> Reservation.create(1L, date, time, theme, member))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 유효하지 않은 사용자입니다.");
    }

    @Test
    @DisplayName("time 값이 null이나 공백을 입력할 경우, 예외가 발생한다.")
    void validationTime() {
        //given
        Theme theme = new Theme(1L, "테스트", "테스트", "테스트");
        ReservationTime time = null;
        LocalDate date = LocalDate.now();
        Member member = new Member(1L, "테스트", "test@example.com", "테스트");
        //when & then
        assertThatThrownBy(() -> Reservation.create(1L, date, time, theme, member))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 유효하지 않은 예약 시간입니다.");
    }

    @Test
    @DisplayName("DateTime 값이 과거일 경우, 예외가 발생한다.")
    void createIfDateTimeValid() {
        //given
        LocalDate date = LocalDate.MIN;
        ReservationTime time = new ReservationTime(1L, LocalTime.MIN);
        Theme theme = new Theme(1L, "테스트", "테스트", "테스트");
        Member member = new Member(1L, "테스트", "test@example.com", "테스트");
        //when & then
        assertThatThrownBy(() -> Reservation.createIfDateTimeValid(date, time, theme, member))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("[ERROR] 예약이 불가능한 시간입니다: ");
    }

}
