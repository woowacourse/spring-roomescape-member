package roomescape.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ReservationTest {

    @Test
    @DisplayName("정상적인 값을 입력하면 예약 객체가 생성된다.")
    void create_ValidParameters_CreatesReservation() {
        ReservationTime time = new ReservationTime(1L, LocalTime.of(10, 0));
        Reservation reservation = new Reservation(1L, "브라운", LocalDate.now().plusDays(1), time);
        assertThat(reservation.name()).isEqualTo("브라운");
    }

    @Test
    @DisplayName("예약자 이름이 null이거나 비어있으면 예외가 발생한다.")
    void create_InvalidName_ThrowsException() {
        ReservationTime time = new ReservationTime(1L, LocalTime.of(10, 0));
        assertThatThrownBy(() -> new Reservation(1L, " ", LocalDate.now().plusDays(1), time))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("예약 날짜가 과거 날짜이면 예외가 발생한다.")
    void create_PastDate_ThrowsException() {
        ReservationTime time = new ReservationTime(1L, LocalTime.of(10, 0));
        LocalDate pastDate = LocalDate.now().minusDays(1);
        assertThatThrownBy(() -> new Reservation(1L, "브라운", pastDate, time))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("예약 시간 객체가 null이면 예외가 발생한다.")
    void create_NullTime_ThrowsException() {
        assertThatThrownBy(() -> new Reservation(1L, "브라운", LocalDate.now().plusDays(1), null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("transientOf를 통해 비영속 상태의 예약 객체를 생성할 수 있다.")
    void transientOf_ValidParameters_CreatesTransientReservation() {
        Reservation reservation = Reservation.transientOf("브라운", LocalDate.now().plusDays(1), new ReservationTime(1L, LocalTime.of(10, 0)));
        assertThat(reservation.id()).isNull();
    }
}
