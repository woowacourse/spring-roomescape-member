package roomescape.time.domain;

import org.junit.jupiter.api.Test;
import roomescape.common.validate.InvalidInputException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class ReservationTimeTest {

    @Test
    void cannotNullTime() {
        // given
        // when
        // then
        assertThatThrownBy(() -> ReservationTime.withoutId(null))
                .isInstanceOf(InvalidInputException.class)
                .hasMessage("Validation failed [while checking null]: ReservationTime.startAt");
    }
}
