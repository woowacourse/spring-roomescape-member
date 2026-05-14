package roomescape.domain.theme;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class ThemeTest {
    @Test
    void 이름이_NULL인_경우_예외가_발생한다() {
        Assertions.assertThatThrownBy(() -> Theme.of(null, "description", "https://zeze.com"))
                .isInstanceOf(NullPointerException.class);
    }
}
