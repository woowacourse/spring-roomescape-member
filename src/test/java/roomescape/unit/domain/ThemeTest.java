package roomescape.unit.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import roomescape.theme.domain.Theme;

public class ThemeTest {

    @Test
    void name이_null이면_예외를_던진다() {
        assertThatThrownBy(() ->
                new Theme(1L, null, "정상 설명", "thumb.jpg"))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void description이_null이면_예외를_던진다() {
        assertThatThrownBy(() ->
                new Theme(1L, "공포", null, "thumb.jpg"))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void thumbnail이_null이면_예외를_던진다() {
        assertThatThrownBy(() ->
                new Theme(1L, "공포", "정상 설명", null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void name은_5자를_초과할_수_없다() {
        assertThatThrownBy(() ->
                new Theme(1L, "여섯글자이상", "정상설명", "thumb.jpg"))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void description은_30자를_초과할_수_없다() {
        String tooLong = "이 설명은 삼십자를 넘는 길이입니다. 초과입니다.3333";
        assertThatThrownBy(() ->
                new Theme(1L, "공포", tooLong, "thumb.jpg"))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void thumbnail은_50자를_초과할_수_없다() {
        String tooLong = "123456789012345678901234567890123456789012345678901";
        assertThatThrownBy(() ->
                new Theme(1L, "공포", "설명", tooLong))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void 정상적으로_생성된다() {
        Theme theme = new Theme(1L, "공포", "무서운 테마입니다", "thumb.jpg");

        assertThat(theme.getName()).isEqualTo("공포");
        assertThat(theme.getDescription()).isEqualTo("무서운 테마입니다");
        assertThat(theme.getThumbnail()).isEqualTo("thumb.jpg");
    }
}
