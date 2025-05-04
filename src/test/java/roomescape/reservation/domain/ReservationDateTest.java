package roomescape.reservation.domain;

import org.junit.jupiter.api.Test;
import roomescape.common.validate.InvalidInputException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class ReservationDateTest {

    @Test
    void cannotNullDate() {
        // given
        // when
        // then
        assertThatThrownBy(() -> ReservationDate.from(null))
                .isInstanceOf(InvalidInputException.class)
                .hasMessage("Validation failed [while checking null]: ReservationDate.value");
    }
}
