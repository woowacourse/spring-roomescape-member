package roomescape.domain.theme.validator;

import static org.assertj.core.api.Assertions.assertThatCode;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import roomescape.domain.global.exception.ErrorCode;
import roomescape.domain.global.exception.ErrorDetail;
import roomescape.global.ExceptionAssertions;

class ThemeValidatorTest {

    @Nested
    @DisplayName("validate 테스트")
    class Validate {

        @Test
        @DisplayName("정상 테스트")
        void 성공() {
            String name = "name";
            String description = "description";
            String imageUrl = "imageUrl";

            assertThatCode(() -> ThemeValidator.validate(name, description, imageUrl))
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
                () -> ThemeValidator.validate(name, description, imageUrl),
                ErrorCode.COMMON_INVALID_REQUEST,
                expectedErrors
            );
        }
    }

}