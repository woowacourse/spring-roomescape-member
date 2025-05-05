package roomescape.unit.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import roomescape.theme.domain.Theme;
import roomescape.theme.domain.ThemeName;
import roomescape.theme.domain.ThemeDescription;
import roomescape.theme.domain.ThemeThumbnail;

public class ThemeTest {

    @Test
    void name이_null이면_예외를_던진다() {
        assertThatThrownBy(() ->
                new Theme(1L, null, new ThemeDescription("정상 설명"), new ThemeThumbnail("thumb.jpg")))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void description이_null이면_예외를_던진다() {
        assertThatThrownBy(() ->
                new Theme(1L, new ThemeName("공포"), null, new ThemeThumbnail("thumb.jpg")))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void thumbnail이_null이면_예외를_던진다() {
        assertThatThrownBy(() ->
                new Theme(1L, new ThemeName("공포"), new ThemeDescription("정상 설명"), null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void 정상적으로_생성된다() {
        Theme theme = new Theme(
                1L,
                new ThemeName("공포"),
                new ThemeDescription("무서운 테마입니다"),
                new ThemeThumbnail("thumb.jpg")
        );

        assertThat(theme.getName().name()).isEqualTo("공포");
        assertThat(theme.getDescription().description()).isEqualTo("무서운 테마입니다");
        assertThat(theme.getThumbnail().thumbnail()).isEqualTo("thumb.jpg");
    }
}
