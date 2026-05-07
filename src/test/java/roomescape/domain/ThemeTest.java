package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import roomescape.domain.vo.ThemeName;

class ThemeTest {

    @Test
    void 아이디가_같으면_동일한_객체이다() {
        // given
        Theme one = new Theme(1L, new ThemeName("이름"), "설명", "image-url");
        Theme other = new Theme(1L, new ThemeName("다른이름"), "다른설명", "other-image-url");

        // when & then
        assertThat(one).isEqualTo(other);
    }

    @Test
    void 아이디가_다르면_다른_객체이다() {
        // given
        Theme one = new Theme(1L, new ThemeName("이름"), "설명", "image-url");
        Theme other = new Theme(2L, new ThemeName("다른이름"), "다른설명", "other-image-url");

        // when & then
        assertThat(one).isNotEqualTo(other);
    }

    @Test
    void 아이디가_null이면_다른_객체이다() {
        // given
        Theme one = new Theme(null, new ThemeName("이름"), "설명", "image-url");
        Theme other = new Theme(null, new ThemeName("이름"), "설명", "image-url");

        // when & then
        assertThat(one).isNotEqualTo(other);
    }

}