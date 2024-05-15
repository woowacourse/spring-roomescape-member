package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import roomescape.exception.InvalidReservationException;

class DescriptionTest {
    @DisplayName("Description length 테스트")
    @ParameterizedTest
    @ValueSource(strings = {"",
            "linirinilinirinilinirinilinirinilinirinilinirinilinirinilinirinilinirinilinirinilinirinilinirinilinirinilinirinilinirinilinirinilinirinilinirinilinirinilinirini"})
    void invalidNameLength(String description) {
        assertThatThrownBy(() -> new Description(description))
                .isInstanceOf(InvalidReservationException.class);
    }
}
