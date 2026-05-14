package roomescape.domain.theme.validator;

import static org.assertj.core.api.Assertions.assertThatCode;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import roomescape.domain.global.exception.error.ErrorCode;
import roomescape.domain.global.exception.error.ErrorDetail;
import roomescape.global.ExceptionAssertions;

class ThemePopularGetRequestValidatorTest {

    @Nested
    @DisplayName("validate 테스트")
    class Validate {

        @Test
        @DisplayName("limit은 0 또는 양수여야 한다.")
        void 성공() {
            Integer limit = 0;

            assertThatCode(() -> ThemePopularGetRequestValidator.validate(limit))
                .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("limit이 음수이면 예외가 발생한다.")
        void 실패1() {
            Integer wrongLimit = -1;
            List<ErrorDetail> expectedErrors = List.of(
                ErrorDetail.of("limit", wrongLimit, "limit은 0 또는 양수여야 합니다."));

            ExceptionAssertions.assertErrorCodeWithErrors(
                () -> ThemePopularGetRequestValidator.validate(wrongLimit),
                ErrorCode.COMMON_INVALID_REQUEST,
                expectedErrors
            );
        }
    }

}