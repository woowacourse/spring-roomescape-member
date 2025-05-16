package roomescape.dto;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import roomescape.dto.request.ThemeCreationRequest;

@Disabled
class ThemeCreationRequestTest {


    @DisplayName("null이거나 공백인 이름을 허용하지 않는다")
    @ParameterizedTest
    @NullAndEmptySource
    void validateFieldName(String invalidName) {
        assertThatThrownBy(() -> new ThemeCreationRequest(invalidName, "설명", "섬네일"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 이름은 빈 값이나 공백값을 허용하지 않습니다.");
    }

    @DisplayName("null이거나 공백인 설명을 허용하지 않는다")
    @ParameterizedTest
    @NullAndEmptySource
    void validateFieldDescription(String invalidDescription) {
        assertThatThrownBy(() -> new ThemeCreationRequest("이름", invalidDescription, "섬네일"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 설명은 빈 값이나 공백값을 허용하지 않습니다.");
    }

    @DisplayName("null이거나 공백인 썸네일을 허용하지 않는다")
    @ParameterizedTest
    @NullAndEmptySource
    void validateFieldThumbnail(String invalidThumbnail) {
        assertThatThrownBy(() -> new ThemeCreationRequest("이름", "설명", invalidThumbnail))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 썸네일은 빈 값이나 공백값을 허용하지 않습니다.");
    }
}