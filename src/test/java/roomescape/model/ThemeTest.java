package roomescape.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ThemeTest {

    @Test
    void 테마_이름이_공백이면_에러가_발생한다() {
        assertThatThrownBy(() -> new Theme(" ", "설명", "url"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("테마 이름은 필수이며, 공백일 수 없습니다.");
    }

    @Test
    void 테마가_정상_생성되면_에러가_발생하지_않는다() {
        assertThatCode(() -> new Theme("공포", "설명", "url"))
                .doesNotThrowAnyException();
    }
}
