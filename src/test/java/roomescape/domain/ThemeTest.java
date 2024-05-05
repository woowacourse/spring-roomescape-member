package roomescape.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ThemeTest {

    @DisplayName("id값을 누락하고 생성 시도를 하면 예외가 발생한다.")
    @Test
    void throwExceptionWhenIdIsNull() {
        // When & Then
        assertThatThrownBy(() -> Theme.of(null, "치킨", "치킨은 맛있다.", "치킨 사진"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("id는 필수 값입니다.");
    }
}
