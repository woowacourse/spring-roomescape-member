package roomescape.domain.theme.dto.request;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ThemeCreateRequestDtoTest {

    @Nested
    class Constructor {

        @Test
        @DisplayName("정상 테스트")
        void 성공() {
            String name = "테마명";
            String description = "테마 설명";
            String imageUrl = "This is image url";

            assertThatCode(() -> new ThemeCreateRequestDto(name, description, imageUrl))
                .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("테마명이 비어 있는 경우 예외가 발생한다.")
        void 실패1() {
            String name = "";
            String description = "테마 설명";
            String imageUrl = "This is image url";

            assertThatThrownBy(() -> new ThemeCreateRequestDto(name, description, imageUrl))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("테마명은 빈 문자열일 수 없습니다.");
        }

        @Test
        @DisplayName("테마 설명이 비어 있는 경우 예외가 발생한다.")
        void 실패2() {
            String name = "테마명";
            String description = "";
            String imageUrl = "This is image url";

            assertThatThrownBy(() -> new ThemeCreateRequestDto(name, description, imageUrl))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("테마 설명은 빈 문자열일 수 없습니다.");
        }

        @Test
        @DisplayName("이미지 url이 비어 있는 경우 예외가 발생한다.")
        void 실패3() {
            String name = "테마명";
            String description = "테마 설명";
            String imageUrl = "";

            assertThatThrownBy(() -> new ThemeCreateRequestDto(name, description, imageUrl))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미지 url은 빈 문자열일 수 없습니다.");
        }
    }

}