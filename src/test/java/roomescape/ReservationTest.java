package roomescape;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.Test;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;

class ReservationTest {

    @Test
    void 이름이_10자_미만이면_예약을_생성한다() {
        Reservation reservation = new Reservation(
                1L,
                "브라운",
                LocalDate.of(2023, 8, 5),
                new ReservationTime(1L, LocalTime.of(10, 0))
        );

        assertThat(reservation.getId()).isEqualTo(1L);
        assertThat(reservation.getName()).isEqualTo("브라운");
        assertThat(reservation.getDate()).isEqualTo(LocalDate.of(2023, 8, 5));
        assertThat(reservation.getTime().getId()).isEqualTo(1L);
        assertThat(reservation.getTime().getStartAt()).isEqualTo(LocalTime.of(10, 0));
    }

    @Test
    void 이름이_10자_이상이면_예외가_발생한다() {
        assertThatThrownBy(() -> new Reservation(
                1L,
                "1234567890",
                LocalDate.of(2023, 8, 5),
                new ReservationTime(1L, LocalTime.of(10, 0))
        )).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 잘못된 이름 입력입니다.");
    }
}
