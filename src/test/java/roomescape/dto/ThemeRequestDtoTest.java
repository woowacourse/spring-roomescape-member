package roomescape.dto;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ThemeRequestDtoTest {

    @Test
    @DisplayName("name 필드가 null일 경우 예외가 발생한다.")
    void failIfNameFieldIsNull() {
        assertThatThrownBy(() -> {
            new ThemeRequestDto(null, "description", "thumbnail");
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("잘못된 name 입력입니다.");
    }

    @Test
    @DisplayName("description 필드가 null일 경우 예외가 발생한다.")
    void failIfDescriptionFieldIsNull() {
        assertThatThrownBy(() -> {
            new ThemeRequestDto("moda", null, "thumbnail");
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("잘못된 description 입력입니다.");
    }

    @Test
    @DisplayName("thumbnail 필드가 null일 경우 예외가 발생한다.")
    void failIfThumbnailFieldIsNull() {
        assertThatThrownBy(() -> {
            new ThemeRequestDto("moda", "description", null);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("잘못된 thumbnail 입력입니다.");
    }
}
