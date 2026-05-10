package roomescape.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class ThemeTest {

    @ParameterizedTest
    @ValueSource(strings = {"", " "})
    void nameBlankExceptionTest(String name) {
        assertThatThrownBy(() -> new Theme(name, "모험 이야기", "url.jpg"))
                .hasMessage("[ERROR] 이름은 비어 있을 수 없습니다.")
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " "})
    void descriptionBlankExceptionTest(String description) {
        assertThatThrownBy(() -> new Theme("피즈의 모험", description, "url.jpg"))
                .hasMessage("[ERROR] 설명은 비어 있을 수 없습니다.")
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " "})
    void thumbnailUrlBlankExceptionTest(String thumbnailUrl) {
        assertThatThrownBy(() -> new Theme("피즈의 모험", "모험 이야기", thumbnailUrl))
                .hasMessage("[ERROR] 썸네일은 비어 있을 수 없습니다.")
                .isInstanceOf(IllegalArgumentException.class);
    }
}
