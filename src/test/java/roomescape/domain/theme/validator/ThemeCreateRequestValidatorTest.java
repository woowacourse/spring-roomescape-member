package roomescape.domain.theme.validator;

import static org.assertj.core.api.Assertions.assertThatCode;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import roomescape.domain.global.exception.error.ErrorCode;
import roomescape.domain.global.exception.error.ErrorDetail;
import roomescape.global.ExceptionAssertions;

class ThemeCreateRequestValidatorTest {

    @Nested
    @DisplayName("validate 테스트")
    class Validate {

        @Test
        @DisplayName("정상 테스트")
        void 성공() {
            String name = "name";
            String description = "description";
            String imageUrl = "imageUrl";

            assertThatCode(() -> ThemeCreateRequestValidator.validate(name, description, imageUrl))
                .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("이름이 비어있는 경우 예외가 발생한다.")
        void 실패1() {
            String name = "";
            String description = "description";
            String imageUrl = "imageUrl";
            List<ErrorDetail> expectedErrors = List.of(
                ErrorDetail.of("name", "", "테마명은 비어있지 않은 문자열이어야 합니다."));

            ExceptionAssertions.assertErrorCodeWithErrors(
                () -> ThemeCreateRequestValidator.validate(name, description, imageUrl),
                ErrorCode.COMMON_INVALID_REQUEST,
                expectedErrors
            );
        }

        @Test
        @DisplayName("테마 설명이 비어있는 경우 예외가 발생한다.")
        void 실패2() {
            String name = "name";
            String description = "";
            String imageUrl = "imageUrl";
            List<ErrorDetail> expectedErrors = List.of(
                ErrorDetail.of("description", "", "테마 설명은 비어있지 않은 문자열이어야 합니다."));

            ExceptionAssertions.assertErrorCodeWithErrors(
                () -> ThemeCreateRequestValidator.validate(name, description, imageUrl),
                ErrorCode.COMMON_INVALID_REQUEST,
                expectedErrors
            );
        }

        @Test
        @DisplayName("이미지 url이 비어있는 경우 예외가 발생한다.")
        void 실패3() {
            String name = "name";
            String description = "description";
            String imageUrl = "";
            List<ErrorDetail> expectedErrors = List.of(
                ErrorDetail.of("imageUrl", "", "썸네일 url은 비어있지 않은 문자열이어야 합니다."));

            ExceptionAssertions.assertErrorCodeWithErrors(
                () -> ThemeCreateRequestValidator.validate(name, description, imageUrl),
                ErrorCode.COMMON_INVALID_REQUEST,
                expectedErrors
            );
        }

        @Test
        @DisplayName("여러 필드가 비어있는 경우 예외가 발생한다.")
        void 실패4() {
            List<ErrorDetail> expectedErrors = List.of(
                ErrorDetail.of("name", "", "테마명은 비어있지 않은 문자열이어야 합니다."),
                ErrorDetail.of("description", "", "테마 설명은 비어있지 않은 문자열이어야 합니다."),
                ErrorDetail.of("imageUrl", "", "썸네일 url은 비어있지 않은 문자열이어야 합니다."));

            ExceptionAssertions.assertErrorCodeWithErrors(
                () -> ThemeCreateRequestValidator.validate("", "", ""),
                ErrorCode.COMMON_INVALID_REQUEST,
                expectedErrors
            );
        }
    }

}
