package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import roomescape.domain.vo.ThemeImageUrl;
import roomescape.domain.vo.ThemeName;

class ThemeTest {

    @Test
    void 아이디가_같으면_동일한_객체이다() {
        // given
        Theme theme = theme().withId(1L);
        Theme sameIdTheme = theme().withId(1L);

        // when & then
        assertThat(theme).isEqualTo(sameIdTheme);
    }

    @Test
    void 아이디가_다르면_다른_객체이다() {
        // given
        Theme theme = theme().withId(1L);
        Theme differentIdTheme = theme().withId(2L);

        // when & then
        assertThat(theme).isNotEqualTo(differentIdTheme);
    }

    @Test
    void withId로_아이디를_부여한_새로운_예약을_생성한다() {
        // given
        Theme theme = theme();

        // when
        Theme saved = theme.withId(1L);

        // then
        assertThat(saved.getId()).isEqualTo(1L);
    }

    @Test
    void 아이디가_null이면_다른_객체이다() {
        // given
        Theme one = theme();
        Theme other = theme();

        // when & then
        assertThat(one).isNotEqualTo(other);
    }

    private Theme theme() {
        return new Theme(
            null,
            new ThemeName("n"),
            "d",
            ThemeImageUrl.defaultImageUrl()
        );
    }
}
