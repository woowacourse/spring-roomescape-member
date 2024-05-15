package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import roomescape.exception.InvalidReservationException;

class ThemeNameTest {
    @DisplayName("ThemeName length 테스트")
    @ParameterizedTest
    @ValueSource(strings = {"",
            "linirinilinirinilinirinilinirinilinirinilinirinilinirinilinirinilinirinilinirinilinirinilinirinilinirinilinirinilinirinilinirinilinirinilinirinilinirinilinirini"})
    void invalidDescriptionLength(String themeName) {
        assertThatThrownBy(() -> new ThemeName(themeName))
                .isInstanceOf(InvalidReservationException.class);
    }
}
