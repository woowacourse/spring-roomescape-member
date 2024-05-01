package roomescape.reservation.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.theme.domain.Theme;
import roomescape.time.domain.ReservationTime;

class ReservationTest {

    @Test
    @DisplayName("동일한 id 가 있다면 true를 반환한다")
    void isSameIdTest() {
        Theme theme = new Theme(new Name("공포"), "무서운 테마", "https://i.pinimg.com/236x.jpg");
        ReservationTime reservationTime = new ReservationTime(1L, LocalTime.now());
        Reservation reservation = new Reservation(1L, new Name("hogi"), LocalDate.now(), theme, reservationTime);
        assertAll(
                () -> assertThat(reservation.isSameId(1L)).isTrue(),
                () -> assertThat(reservation.isSameId(2L)).isFalse()
        );
    }
}
