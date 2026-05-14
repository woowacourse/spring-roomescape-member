package roomescape.domain.reservation.validator;

import static org.assertj.core.api.Assertions.assertThatCode;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import roomescape.domain.global.exception.error.ErrorCode;
import roomescape.domain.global.exception.error.ErrorDetail;
import roomescape.global.ExceptionAssertions;

class ReservationCreateRequestValidatorTest {

    @Nested
    @DisplayName("validate 테스트")
    class ValidateTest {

        @Test
        @DisplayName("이름이 비어있지 않으면 예외가 발생하지 않는다.")
        void 성공() {
            assertThatCode(() -> ReservationCreateRequestValidator.validate("브라운"))
                .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("이름이 비어있으면 예외가 발생한다.")
        void 실패() {
            List<ErrorDetail> expectedErrors = List.of(
                ErrorDetail.of("name", "", "이름은 비어있지 않은 문자열이어야 합니다."));

            ExceptionAssertions.assertErrorCodeWithErrors(
                () -> ReservationCreateRequestValidator.validate(""),
                ErrorCode.COMMON_INVALID_REQUEST,
                expectedErrors
            );
        }
    }
}
