package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import roomescape.domain.theme.Theme;
import roomescape.global.exception.RoomescapeException;

class ThemeTest {

    @DisplayName("성공: 올바른 값 입력하여 생성")
    @Test
    void construct() {
        assertThatCode(() -> new Theme("name", "description", "https://"))
            .doesNotThrowAnyException();
    }

    @DisplayName("실패: 올바르지 않은 테마명")
    @ParameterizedTest
    @ValueSource(strings = {" "})
    @NullAndEmptySource
    void construct_IllegalName(String name) {
        assertThatThrownBy(() -> new Theme(name, "description", "https://"))
            .isInstanceOf(RoomescapeException.class)
            .hasMessage("테마 이름은 null이거나 비어 있을 수 없습니다.");
    }

    @DisplayName("실패: 올바르지 않은 테마 설명")
    @ParameterizedTest
    @ValueSource(strings = {" "})
    @NullAndEmptySource
    void construct_IllegalDescription(String description) {
        assertThatThrownBy(() -> new Theme("name", description, "https://"))
            .isInstanceOf(RoomescapeException.class)
            .hasMessage("테마 설명은 null이거나 비어 있을 수 없습니다.");
    }

    @DisplayName("실패: 올바르지 않은 썸네일 URL")
    @ParameterizedTest
    @ValueSource(strings = {"http://test", "ftp://test"})
    @NullAndEmptySource
    void construct_IllegalThumbnail(String url) {
        assertThatThrownBy(() -> new Theme("name", "description", url))
            .isInstanceOf(RoomescapeException.class)
            .hasMessage("썸네일 URL은 https://로 시작해야 합니다.");
    }
}
