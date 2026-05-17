package roomescape.theme.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import roomescape.exception.BusinessException;

class ThemeTest {

    @Test
    void 이름_입력이_빈값일_경우_예외() {
        // given
        String name = "";

        // when & then
        assertThatThrownBy(() -> new Theme(name, "은하수 테마방입니다.", "http.kkk.jpg"))
            .isInstanceOf(BusinessException.class)
                .hasMessage("테마 이름 형식이 잘못되었습니다.");
    }


    @Test
    void 이름_입력이_범위를_초과할_경우_예외() {
        // given
        String name = "저 멀리있는 은하수에서 외계인이 있다면?";

        // when & then
        assertThatThrownBy(() -> new Theme(name, "은하수 테마방입니다.", "http.kkk.jpg"))
                .isInstanceOf(BusinessException.class)
                .hasMessage("테마 이름 형식이 잘못되었습니다.");
    }

}