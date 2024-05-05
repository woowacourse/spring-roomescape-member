package roomescape.service.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import roomescape.service.dto.request.SaveThemeRequest;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class SaveThemeRequestTest {

    @Test
    @DisplayName("테마 이름이 정상 입력될 경우 성공한다.")
    void checkThemeNameBlank_Success() {
        assertThatCode(() -> new SaveThemeRequest("capy", "description", "thumbnail"))
                .doesNotThrowAnyException();
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "  "})
    @DisplayName("테마 이름이 빈칸인 경우 예외가 발생한다.")
    void checkThemeNameBlank_Failure(String name) {
        assertThatThrownBy(() -> new SaveThemeRequest(name, "description", "thumbnail"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("테마 이름은 빈칸일 수 없습니다.");
    }
}
