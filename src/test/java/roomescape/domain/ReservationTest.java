package roomescape.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.*;

class ReservationTest {

    @Test
    @DisplayName("성공 : id를 통해 동일한 예약인지 판별한다.")
    void checkSameReservation_Success() {
        Reservation reservation1 = new Reservation(1L, "capy", LocalDate.now().plusDays(1L), new ReservationTime(LocalTime.of(10, 0)));
        Reservation reservation2 = new Reservation(1L, "capy", LocalDate.now().plusDays(1L), new ReservationTime(LocalTime.of(10, 0)));

        assertThat(reservation1.isSameReservation(reservation2.getId())).isTrue();
    }

    @Test
    @DisplayName("실패 : id를 통해 동일한 예약인지 판별한다.")
    void checkSameReservation_Failure() {
        Reservation reservation1 = new Reservation(1L, "capy", LocalDate.now().plusDays(1L), new ReservationTime(LocalTime.of(10, 0)));
        Reservation reservation2 = new Reservation(2L, "capy", LocalDate.now().plusDays(1L), new ReservationTime(LocalTime.of(11, 0)));

        assertThat(reservation1.isSameReservation(reservation2.getId())).isFalse();
    }

    @Test
    @DisplayName("미래의 날짜와 시간에 대한 예약 생성이 가능하다.")
    void checkReservationDateTimeIsFuture_Success() {
        assertThatCode(() -> new Reservation(1L, "capy", LocalDate.now().plusDays(1L), new ReservationTime(LocalTime.now())))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("지나간 날짜와 시간에 대한 예약 생성시 예외가 발생한다.")
    void checkReservationDateTimeIsFuture_Failure() {
        assertThatThrownBy(() -> new Reservation(1L, "capy", LocalDate.now().minusDays(1L), new ReservationTime(LocalTime.now())))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("지나간 날짜와 시간에 대한 예약 생성은 불가능합니다.");
    }
}
