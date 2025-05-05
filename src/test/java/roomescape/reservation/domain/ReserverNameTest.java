package roomescape.reservation.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.common.validate.InvalidInputException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class ReserverNameTest {

    @Test
    @DisplayName("예약자 이름은 비어있을 수 없다")
    void cannotEmptyReserverName() {
        // given
        // when
        // then
        assertAll(() -> {
            assertThatThrownBy(() -> ReserverName.from(null))
                    .isInstanceOf(InvalidInputException.class)
                    .hasMessage("Validation failed [while checking blank]: ReserverName.value");
            assertThatThrownBy(() -> ReserverName.from(""))
                    .isInstanceOf(InvalidInputException.class)
                    .hasMessage("Validation failed [while checking blank]: ReserverName.value");
        });
    }
}
