package roomescape.unit.domain;

import java.time.LocalDate;
import java.time.LocalTime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ReservationTest {
    
    @Test
    void 예약은_공백이거나_NULL_로_이루어질_수_없다() {
        Assertions.assertThatThrownBy(
                        () -> new Reservation(1L, "", LocalDate.now(), new ReservationTime(1L, LocalTime.now())))
                .isInstanceOf(IllegalArgumentException.class);
    }
}

