package roomescape.theme.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ThemeTest {

    @Test
    @DisplayName("정상적인 데이터로 테마 객체를 생성한다.")
    void createSuccess() {
        assertThatCode(() -> new Theme(1L, "우테코 방탈출", "재미있는 방탈출입니다.", "https://example.com/image.png"))
                .doesNotThrowAnyException();
    }

    @Nested
    @DisplayName("이름 검증 테스트")
    class NameValidationTest {

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {" ", "  "})
        @DisplayName("이름이 비어있거나 공백이면 예외가 발생한다.")
        void failWhenNameIsBlank(String name) {
            assertThatThrownBy(() -> new Theme(null, name, "설명", "url"))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("테마 이름은(는) 비어 있을 수 없습니다.");
        }

        @Test
        @DisplayName("이름이 255자를 초과하면 예외가 발생한다.")
        void failWhenNameIsTooLong() {
            String longName = "a".repeat(256);
            assertThatThrownBy(() -> new Theme(null, longName, "설명", "url"))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("테마 이름은(는) 255자 이하여야 합니다.");
        }
    }

    @Nested
    @DisplayName("설명 검증 테스트")
    class DescriptionValidationTest {

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("설명이 비어있으면 예외가 발생한다.")
        void failWhenDescriptionIsBlank(String description) {
            assertThatThrownBy(() -> new Theme(null, "이름", description, "url"))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("테마 설명은(는) 비어 있을 수 없습니다.");
        }

        @Test
        @DisplayName("설명이 255자를 초과하면 예외가 발생한다.")
        void failWhenDescriptionIsTooLong() {
            String longDescription = "b".repeat(256);
            assertThatThrownBy(() -> new Theme(null, "이름", longDescription, "url"))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("테마 설명은(는) 255자 이하여야 합니다.");
        }
    }

    @Nested
    @DisplayName("썸네일 URL 검증 테스트")
    class ThumbnailUrlValidationTest {

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("썸네일 URL이 비어있으면 예외가 발생한다.")
        void failWhenUrlIsBlank(String url) {
            assertThatThrownBy(() -> new Theme(null, "이름", "설명", url))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("테마 썸네일 URL은(는) 비어 있을 수 없습니다.");
        }

        @Test
        @DisplayName("썸네일 URL이 1024자를 초과하면 예외가 발생한다.")
        void failWhenUrlIsTooLong() {
            String longUrl = "c".repeat(1025);
            assertThatThrownBy(() -> new Theme(null, "이름", "설명", longUrl))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("테마 썸네일 URL은(는) 1024자 이하여야 합니다.");
        }
    }
}
