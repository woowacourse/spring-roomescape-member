package roomescape.reservation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.Test;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;

class ReservationTest {

    @Test
    void 예약_생성() {
        ReservationTime time = new ReservationTime(1L, LocalTime.of(15, 40));
        Reservation reservation = new Reservation(1L, "브라운", LocalDate.of(2023, 8, 5), time);

        assertThat(reservation.getId()).isEqualTo(1L);
        assertThat(reservation.getName()).isEqualTo("브라운");
        assertThat(reservation.getDate()).isEqualTo(LocalDate.of(2023, 8, 5));
        assertThat(reservation.getTime()).isEqualTo(time);
    }

    @Test
    void 이름이_null이면_예외() {
        ReservationTime time = new ReservationTime(1L, LocalTime.of(15, 40));

        assertThatThrownBy(() -> new Reservation(1L, null, LocalDate.of(2023, 8, 5), time))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 이름이_공백이면_예외() {
        ReservationTime time = new ReservationTime(1L, LocalTime.of(15, 40));

        assertThatThrownBy(() -> new Reservation(1L, "   ", LocalDate.of(2023, 8, 5), time))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 날짜가_null이면_예외() {
        ReservationTime time = new ReservationTime(1L, LocalTime.of(15, 40));

        assertThatThrownBy(() -> new Reservation(1L, "브라운", null, time))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 시간이_null이면_예외() {
        assertThatThrownBy(() -> new Reservation(1L, "브라운", LocalDate.of(2023, 8, 5), null))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
