package roomescape.domain;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import roomescape.domain.theme.Theme;
import roomescape.exception.EmptyParameterException;

@DisplayName("테마")
class ThemeTest {

    @DisplayName("테마 생성 시 이름, 설명, 썸네일 중 하나라도 비어있을 경우 예외가 발생한다.")
    @ValueSource(strings = {"", " ", "    ", "\n", "\r", "\t"})
    @ParameterizedTest
    void validateNotBlank(String blank) {
        // when & then
        String expectedMessage = "테마의 정보는 비어있을 수 없습니다.";

        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThatThrownBy(() -> new Theme(blank, "description", "thumbnail"))
                .isInstanceOf(EmptyParameterException.class)
                .hasMessage(expectedMessage);
        softAssertions.assertThatThrownBy(() -> new Theme("name", blank, "thumbnail"))
                .isInstanceOf(EmptyParameterException.class)
                .hasMessage(expectedMessage);
        softAssertions.assertThatThrownBy(() -> new Theme("name", "description", blank))
                .isInstanceOf(EmptyParameterException.class)
                .hasMessage(expectedMessage);
        softAssertions.assertAll();
    }
}
