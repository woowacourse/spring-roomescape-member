package roomescape.unit.domain;

import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.Test;
import roomescape.reservation.domain.ReservationDate;

public class ReservationDateTest {

    @Test
    void reservationDate는_null일_수_없다() {
        assertThatThrownBy(() -> new ReservationDate(null))
                .isInstanceOf(NullPointerException.class);
    }
}
