package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ReservationTest {

    @DisplayName("현재 날짜로 예약을 생성할 때 과거 시간이라면 예약을 생성할 수 없다.")
    @Test
    void when_current_date_and_past_hour_then_throw_exception() {

        //given
        ReservationTime reservationTime = new ReservationTime(1L, LocalTime.of(9, 21));
        ReservationDate reservationDate = new ReservationDate(LocalDate.of(2025, 2, 5));
        Reservation reservation = new Reservation(new Person("james"), reservationDate,
            reservationTime);
        LocalDateTime currentDateTime = LocalDateTime.of(2025, 2, 5, 9, 20);

        //when, then
        assertThatThrownBy(
            () -> reservation.validateDateTime(reservationDate, reservationTime, currentDateTime))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("과거 시간으로는 예약할 수 없습니다.");
    }

    @DisplayName("현재 날짜로 예약을 생성할 때 과거 시간이 아니라면 예약을 생성할 수 있다.")
    @Test
    void when_current_date_and_past_hour_then_success() {

        //given
        ReservationTime reservationTime = new ReservationTime(1L, LocalTime.of(9, 21));
        ReservationDate reservationDate = new ReservationDate(LocalDate.of(2025, 2, 5));
        Reservation reservation = new Reservation(new Person("james"), reservationDate,
            reservationTime);
        LocalDateTime currentDateTime = LocalDateTime.of(2025, 2, 5, 9, 22);

        //when, then
        assertThatCode(
            () -> reservation.validateDateTime(reservationDate, reservationTime, currentDateTime))
            .doesNotThrowAnyException();
    }

}