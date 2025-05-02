package roomescape.unit.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ReservationTest {

    @Test
    void 예약은_공백이거나_NULL_로_이루어질_수_없다() {
        assertThatThrownBy(
                () -> new Reservation(1L, "", LocalDate.now(), new ReservationTime(1L, LocalTime.now()),
                        new Theme(1L, "테마", "설명", "썸네일")))
                .isInstanceOf(IllegalArgumentException.class);
    }
}

