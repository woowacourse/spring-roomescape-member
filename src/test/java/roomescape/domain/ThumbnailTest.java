package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import roomescape.exception.InvalidReservationException;

class ThumbnailTest {
    @DisplayName("Thumbnail length 테스트")
    @ParameterizedTest
    @ValueSource(strings = {"",
            "linirinilinirinilinirinilinirinilinirinilinirinilinirinilinirinilinirinilinirinilinirinilinirinilinirinilinirinilinirinilinirinilinirinilinirinilinirinilinirinilinirinilinirinilinirinilinirinilinirinilinirinilinirinilinirinilinirinilinirinilinirinilinirinilinirinilinirinilinirinilinirinilinirinilinirinilinirinilinirini"})
    void invalidDescriptionLength(String thumbnail) {
        assertThatThrownBy(() -> new Thumbnail(thumbnail))
                .isInstanceOf(InvalidReservationException.class);
    }
}
