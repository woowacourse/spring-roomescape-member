package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.UUID;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ThemeTest {

    private static final UUID DEFAULT_ID = UUID.fromString("00000000-0000-0000-0000-000000000001");
    private static final String DEFAULT_NAME = "name";
    private static final String DEFAULT_DESCRIPTION = "description";
    private static final String DEFAULT_IMAGE_URL = "imageUrl";

    @Nested
    class 생성_시_정보를_검증한다 {

        @Test
        void 식별자가_없다면_예외를_던진다() {
            assertThatThrownBy(() -> new Theme(
                    null,
                    DEFAULT_NAME,
                    DEFAULT_DESCRIPTION,
                    DEFAULT_IMAGE_URL
            )).isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("테마엔 식별자가 존재해야 합니다.");
        }

        @Test
        void 이름_정보가_없다면_예외를_던진다() {
            assertThatThrownBy(() -> new Theme(
                    DEFAULT_ID,
                    null,
                    DEFAULT_DESCRIPTION,
                    DEFAULT_IMAGE_URL
            )).isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("테마엔 이름이 존재해야 합니다.");
        }

        @Test
        void 설명_정보가_없다면_예외를_던진다() {
            assertThatThrownBy(() -> new Theme(
                    DEFAULT_ID,
                    DEFAULT_NAME,
                    null,
                    DEFAULT_IMAGE_URL
            )).isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("테마엔 설명이 존재해야 합니다.");
        }

        @Test
        void 이미지_정보가_없다면_예외를_던진다() {
            assertThatThrownBy(() -> new Theme(
                    DEFAULT_ID,
                    DEFAULT_NAME,
                    DEFAULT_DESCRIPTION,
                    null
            )).isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("테마엔 이미지가 존재해야 합니다.");
        }
    }
}
