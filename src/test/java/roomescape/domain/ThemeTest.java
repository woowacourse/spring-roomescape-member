package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import roomescape.domain.vo.Name;

class ThemeTest {

    private static final Name NAME = new Name("방탈출");
    private static final String URL = "http://example.com/img.jpg";
    private static final String DESCRIPTION = "방탈출 테마 설명";

    @Nested
    class Constructor {

        @Test
        @DisplayName("id 없이 생성하면 id가 null이다")
        void createsThemeWithNullId() {
            assertThat(new Theme(NAME, URL, DESCRIPTION).getId()).isNull();
        }

        @Test
        @DisplayName("id와 함께 생성하면 모든 필드가 저장된다")
        void createsThemeWithId() {
            Theme theme = new Theme(1L, NAME, URL, DESCRIPTION);

            assertThat(theme.getId()).isEqualTo(1L);
            assertThat(theme.getName()).isEqualTo(NAME);
            assertThat(theme.getThumbnailUrl()).isEqualTo(URL);
            assertThat(theme.getDescription()).isEqualTo(DESCRIPTION);
        }
    }
}
