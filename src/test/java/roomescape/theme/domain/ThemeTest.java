package roomescape.theme.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import roomescape.exception.RoomEscapeException;
import roomescape.exception.message.ExceptionMessage;

class ThemeTest {

    @DisplayName("테마 이름이 없거나 공백인 경우 예외가 발생한다.")
    @ParameterizedTest
    @NullAndEmptySource
    void validateInvalidName(final String name) {
        assertThatThrownBy(() -> new Theme(1L, name, "description", "thumbnail"))
                .isInstanceOf(RoomEscapeException.class)
                .hasMessage(ExceptionMessage.INVALID_THEME_NAME.getMessage());
    }
}
