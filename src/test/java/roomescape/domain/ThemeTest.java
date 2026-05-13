package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ThemeTest {

    @Test
    @DisplayName("올바른 정보로 테마를 생성하면 성공한다.")
    void 정상_생성_테스트() {
        Long id = 1L;
        String name = "우테코";
        String description = "우테코";
        String url = "/woowacourse";

        assertDoesNotThrow(() -> new Theme(id, name, description, url));
    }

    @Test
    @DisplayName("테마 이름이 null이면 예외가 발생한다")
    void 이름_null_예외_테스트() {
        Long id = 1L;
        String name = null;
        String description = "우테코";
        String url = "/woowacourse";

        assertThatThrownBy(() -> new Theme(id, name, description, url))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("테마 이름이 비어 있습니다.");
    }

    @Test
    @DisplayName("테마 이름이 빈칸 이면 예외가 발생한다")
    void 이름_빈칸_예외_테스트() {
        Long id = 1L;
        String name = "";
        String description = "우테코";
        String url = "/woowacourse";

        assertThatThrownBy(() -> new Theme(id, name, description, url))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("테마 이름이 비어 있습니다.");
    }

    @Test
    @DisplayName("테마 설명이 null이면 예외가 발생한다")
    void 설명_null_예외_테스트() {
        Long id = 1L;
        String name = "우테코";
        String description = null;
        String url = "/woowacourse";

        assertThatThrownBy(() -> new Theme(id, name, description, url))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("테마 설명이 비어 있습니다.");
    }

    @Test
    @DisplayName("테마 썸네일 주소가 null이면 예외가 발생한다")
    void 썸네일_주소_null_예외_테스트() {
        Long id = 1L;
        String name = "우테코";
        String description = "우테코";
        String url = null;

        assertThatThrownBy(() -> new Theme(id, name, description, url))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("테마 썸네일 주소가 비어 있습니다.");
    }
}
