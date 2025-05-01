package roomescape.dto;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.exception.InvalidInputException;

class ThemeRequestTest {

    @Test
    @DisplayName("이름은 빈 값이 들어올 수 없다.")
    void validateNullOfName() {
        assertThatThrownBy(() -> new ThemeRequest(null, "설명", "썸네일"))
            .isInstanceOf(InvalidInputException.class)
            .hasMessage("값을 모두 선택해라.");
    }

    @Test
    @DisplayName("설명은 빈 값이 들어올 수 없다.")
    void validateNullOfDescription() {
        assertThatThrownBy(() -> new ThemeRequest("이름", null, "썸네일"))
            .isInstanceOf(InvalidInputException.class)
            .hasMessage("값을 모두 선택해라.");
    }

    @Test
    @DisplayName("썸네일은 빈 값이 들어올 수 없다.")
    void validateNullOfThumbnail() {
        assertThatThrownBy(() -> new ThemeRequest("이름", "설명", null))
            .isInstanceOf(InvalidInputException.class)
            .hasMessage("값을 모두 선택해라.");
    }

    @Test
    @DisplayName("이름은 한 글자 이상이어야 한다")
    void validateNameLength() {
        assertThatThrownBy(() -> new ThemeRequest("", "설명", "썸네일"))
            .isInstanceOf(InvalidInputException.class)
            .hasMessage("입력되지 않은 값이 있다.");
    }

    @Test
    @DisplayName("설명은 한 글자 이상이어야 한다")
    void validateDescriptionLength() {
        assertThatThrownBy(() -> new ThemeRequest("이름", "", "썸네일"))
            .isInstanceOf(InvalidInputException.class)
            .hasMessage("입력되지 않은 값이 있다.");
    }

    @Test
    @DisplayName("썸네일은 한 글자 이상이어야 한다")
    void validateThumbnailLength() {
        assertThatThrownBy(() -> new ThemeRequest("이름", "설명", ""))
            .isInstanceOf(InvalidInputException.class)
            .hasMessage("입력되지 않은 값이 있다.");
    }
}
