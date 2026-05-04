package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class ThemeTest {

    @Test
    void 정상적인_테마_정보를_생성한다() {
        // given
        String name = "공포의 방";
        String description = "정말 무시무시한 공포 테마입니다.";
        String thumbnailImageUrl = "https://image.com/horror.png";

        // when
        Theme theme = new Theme(name, description, thumbnailImageUrl);

        // then
        assertThat(theme)
                .extracting(Theme::getName, Theme::getDescription, Theme::getThumbnailImageUrl)
                .containsExactly(name, description, thumbnailImageUrl);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "  "})
    void 테마_이름이_비어있을_경우_예외가_발생한다(String invalidName) {
        // given
        String description = "설명";
        String thumbnailImageUrl = "https://image.com/test.png";

        // when & then
        assertThatThrownBy(() -> new Theme(invalidName, description, thumbnailImageUrl))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이름은 필수 값입니다.");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "  "})
    void 테마_설명이_비어있을_경우_예외가_발생한다(String invalidDescription) {
        // given
        String name = "테마 이름";
        String thumbnailImageUrl = "https://image.com/test.png";

        // when & then
        assertThatThrownBy(() -> new Theme(name, invalidDescription, thumbnailImageUrl))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("설명은 필수 값입니다.");
    }

    @ParameterizedTest(name = "이미지 주소가 ''{0}''일 때, \"{1}\" 메시지와 함께 예외가 발생한다.")
    @CsvSource(value = {
            "null, 올바른 이미지 주소 형식이 아닙니다.",
            "'', 올바른 이미지 주소 형식이 아닙니다.",
            "' ', 올바른 이미지 주소 형식이 아닙니다.",
            "문자열, 올바른 이미지 주소 형식이 아닙니다.",                       // 순수 문자열
            "ftp://image.com/test.png, 올바른 이미지 주소 형식이 아닙니다.",   // 다른 프로토콜 스킴
            "htts://image.com/test.png, 올바른 이미지 주소 형식이 아닙니다."   // 오탈자
    }, nullValues = "null")
    void 썸네일_이미지_주소_검증_통합_테스트(String invalidUrl, String expectedMessage) {
        // given
        String name = "테마 이름";
        String description = "설명";

        // when & then
        assertThatThrownBy(() -> new Theme(name, description, invalidUrl))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(expectedMessage);
    }
}
