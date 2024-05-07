package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import roomescape.exception.InvalidInputException;

public class RoomThemeTest {
    @DisplayName("정상 생성 테스트")
    @Test
    void validCreate() {
        assertDoesNotThrow(() -> new RoomTheme("name", "desc", "th.jpg"));
    }

    @DisplayName("이름에 빈문자열이 들어왔을 때 예외를 발생시킨다.")
    @ParameterizedTest
    @ValueSource(strings = {"", " "})
    void BlankNameThrowsException(String value) {
        assertThatThrownBy(() -> new RoomTheme(value, "desc", "th.jpg"))
                .isInstanceOf(InvalidInputException.class)
                .hasMessage("테마 이름에 공백을 입력할 수 없습니다.");
    }

    @DisplayName("설명에 빈문자열이 들어왔을 때 예외를 발생시킨다.")
    @ParameterizedTest
    @ValueSource(strings = {"", " "})
    void BlankDescriptionThrowsException(String value) {
        assertThatThrownBy(() -> new RoomTheme("name", value, "th.jpg"))
                .isInstanceOf(InvalidInputException.class)
                .hasMessage("테마 설명에 공백을 입력할 수 없습니다.");
    }

    @DisplayName("썸네일에 빈문자열이 들어왔을 때 예외를 발생시킨다.")
    @ParameterizedTest
    @ValueSource(strings = {"", " "})
    void BlankThumbnailThrowsException(String value) {
        assertThatThrownBy(() -> new RoomTheme("name", "desc", value))
                .isInstanceOf(InvalidInputException.class)
                .hasMessage("테마 썸네일에 공백을 입력할 수 없습니다.");
    }
}
