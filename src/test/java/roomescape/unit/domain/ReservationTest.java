package roomescape.unit.domain;

import static org.assertj.core.api.Assertions.*;
import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.Test;
import roomescape.reservation.domain.Reservation;
import roomescape.time.domain.ReservationTime;
import roomescape.theme.domain.Theme;

public class ReservationTest {

    private final ReservationTime time = new ReservationTime(1L, LocalTime.of(10, 0));
    private final Theme theme = new Theme(1L, "공포", "무서운 테마", "thumbnail");

    @Test
    void reserverName은_null일_수_없다() {
        assertThatThrownBy(() ->
                new Reservation(1L, null, LocalDate.now(), time, theme)
        ).isInstanceOf(NullPointerException.class);
    }

    @Test
    void reservationDate는_null일_수_없다() {
        assertThatThrownBy(() ->
                new Reservation(1L, "홍길동", null, time, theme)
        ).isInstanceOf(NullPointerException.class);
    }

    @Test
    void reservationTime은_null일_수_없다() {
        assertThatThrownBy(() ->
                new Reservation(1L, "홍길동", LocalDate.now(), null, theme)
        ).isInstanceOf(NullPointerException.class);
    }

    @Test
    void theme은_null일_수_없다() {
        assertThatThrownBy(() ->
                new Reservation(1L, "홍길동", LocalDate.now(), time, null)
        ).isInstanceOf(NullPointerException.class);
    }
}
