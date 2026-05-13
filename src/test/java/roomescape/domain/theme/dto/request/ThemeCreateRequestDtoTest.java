package roomescape.domain.theme.dto.request;

import static org.assertj.core.api.Assertions.assertThatCode;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import roomescape.domain.global.exception.BadRequestException;
import roomescape.domain.global.exception.ErrorCode;
import roomescape.global.ExceptionAssertions;

class ThemeCreateRequestDtoTest {

    @Nested
    @DisplayName("생성자 테스트")
    @Disabled
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

            ExceptionAssertions.assertErrorCode(
                () -> new ThemeCreateRequestDto(name, description, imageUrl),
                BadRequestException.class,
                ErrorCode.THEME_INVALID_REQUEST
            );
        }

        @Test
        @DisplayName("테마 설명이 비어 있는 경우 예외가 발생한다.")
        void 실패2() {
            String name = "테마명";
            String description = "";
            String imageUrl = "This is image url";

            ExceptionAssertions.assertErrorCode(
                () -> new ThemeCreateRequestDto(name, description, imageUrl),
                BadRequestException.class,
                ErrorCode.THEME_INVALID_REQUEST
            );
        }

        @Test
        @DisplayName("이미지 url이 비어 있는 경우 예외가 발생한다.")
        void 실패3() {
            String name = "테마명";
            String description = "테마 설명";
            String imageUrl = "";

            ExceptionAssertions.assertErrorCode(
                () -> new ThemeCreateRequestDto(name, description, imageUrl),
                BadRequestException.class,
                ErrorCode.THEME_INVALID_REQUEST
            );
        }
    }

}