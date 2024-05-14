package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static roomescape.util.Fixture.ID_1;
import static roomescape.util.Fixture.LEVEL2_DESCRIPTION;
import static roomescape.util.Fixture.LEVEL2_NAME;
import static roomescape.util.Fixture.LEVEL2_THUMBNAIL;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import roomescape.exception.BadRequestException;

class ThemeTest {
    @Test
    @DisplayName("테마를 생성한다")
    void createTheme() {
        assertThatCode(() -> new Theme(ID_1, LEVEL2_NAME, LEVEL2_DESCRIPTION, LEVEL2_THUMBNAIL))
                .doesNotThrowAnyException();
    }

    @ParameterizedTest
    @DisplayName("테마 생성 시, name이 null이나 빈값이면 예외가 발생한다")
    @NullAndEmptySource
    @ValueSource(strings = {" ", "\n"})
    void throwExceptionWhenEmptyName(final String emptyName) {
        assertThatThrownBy(() -> new Theme(ID_1, emptyName, LEVEL2_DESCRIPTION, LEVEL2_THUMBNAIL))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("테마 이름은 null이나 빈 값일 수 없습니다.");
    }

    @ParameterizedTest
    @DisplayName("테마 생성 시, description이 null이나 빈값이면 예외가 발생한다")
    @NullAndEmptySource
    void throwExceptionWhenEmptyDescription(final String emptyDescription) {
        assertThatThrownBy(() -> new Theme(ID_1, LEVEL2_NAME, emptyDescription, LEVEL2_THUMBNAIL))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("테마 설명은 null이나 빈 값일 수 없습니다.");
    }

    @ParameterizedTest
    @DisplayName("테마 생성 시, thumbnail이 null이나 빈값이면 예외가 발생한다")
    @NullAndEmptySource
    void throwExceptionWhenEmptyThumbnail(final String emptyThumbnail) {
        assertThatThrownBy(() -> new Theme(ID_1, LEVEL2_NAME, LEVEL2_DESCRIPTION, emptyThumbnail))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("테마 이미지는 null이나 빈 값일 수 없습니다.");
    }
}
