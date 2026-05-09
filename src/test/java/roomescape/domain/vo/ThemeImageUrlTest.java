package roomescape.domain.vo;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class ThemeImageUrlTest {

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "  ", "\t", "\n", "\t\n"})
    void 빈_문자열로_생성하면_예외가_발생한다(String value) {
        // when & then
        assertThatThrownBy(() -> new ThemeImageUrl(value))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("빈 문자열은");
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "https://example.com/image",           // 확장자 없음
        "https://example.com/image.pdf",       // 이미지 아닌 확장자
        "ftp://example.com/image.jpg",         // http/https 아님
        "example.com/image.jpg",              // 프로토콜 없음
        "https://",                           // 경로 없음
        "https://example.com/.jpg",           // 파일명 없음
        "not_a_url",                          // URL 형식 아님
        " https://example.com/image.jpg",     // 앞 공백
    })
    void 올바르지_않은_형식으로_생성하면_예외가_발생한다(String value) {
        // when & then
        assertThatThrownBy(() -> new ThemeImageUrl(value))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("올바른 이미지 URL");
    }
}