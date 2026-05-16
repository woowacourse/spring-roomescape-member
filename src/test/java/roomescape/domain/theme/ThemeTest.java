package roomescape.domain.theme;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import org.junit.jupiter.api.Test;
import roomescape.support.exception.RoomescapeException;

class ThemeTest {

    @Test
    void id가_없는_테마를_생성한다() {
        // given
        String name = "미스터리";
        String content = "보예의 미스터리";
        String url = "theme-url";

        // when
        Theme theme = Theme.createWithoutId(name, content, url);

        // then
        assertSoftly(softly -> {
            assertThat(theme.getId()).isNull();
            assertThat(theme.getName()).isEqualTo(name);
            assertThat(theme.getContent()).isEqualTo(content);
            assertThat(theme.getUrl()).isEqualTo(url);
        });
    }

    @Test
    void 이름이_10자를_초과하면_예외가_발생한다() {
        // given
        String name = "공포공포공포공포공포공";

        // when & then
        assertThatThrownBy(() -> Theme.createWithoutId(name, "보예의 미스터리", "theme-url"))
            .isInstanceOf(RoomescapeException.class)
            .hasMessage("테마 이름은 10자 이하여야 합니다.");
    }
}
