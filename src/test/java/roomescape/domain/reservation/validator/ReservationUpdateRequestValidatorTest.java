package roomescape.domain.reservation.validator;

import static org.assertj.core.api.Assertions.assertThatCode;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import roomescape.domain.global.exception.error.ErrorCode;
import roomescape.domain.global.exception.error.ErrorDetail;
import roomescape.global.ExceptionAssertions;

class ReservationUpdateRequestValidatorTest {

    @Nested
    @DisplayName("validate 테스트")
    class ValidateTest {

        @Test
        @DisplayName("날짜와 시간 id가 모두 있으면 예외가 발생하지 않는다.")
        void 성공() {
            assertThatCode(() -> ReservationUpdateRequestValidator.validate(
                LocalDate.of(2026, 5, 1), 1L))
                .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("날짜가 없으면 예외가 발생한다.")
        void 실패1() {
            List<ErrorDetail> expectedErrors = List.of(
                ErrorDetail.of("date", "date가 누락되었습니다."));

            ExceptionAssertions.assertErrorCodeWithErrors(
                () -> ReservationUpdateRequestValidator.validate(null, 1L),
                ErrorCode.COMMON_INVALID_REQUEST_BODY,
                expectedErrors
            );
        }

        @Test
        @DisplayName("시간 id가 없으면 예외가 발생한다.")
        void 실패2() {
            List<ErrorDetail> expectedErrors = List.of(
                ErrorDetail.of("timeId", "timeId가 누락되었습니다."));

            ExceptionAssertions.assertErrorCodeWithErrors(
                () -> ReservationUpdateRequestValidator.validate(LocalDate.of(2026, 5, 1), null),
                ErrorCode.COMMON_INVALID_REQUEST_BODY,
                expectedErrors
            );
        }

        @Test
        @DisplayName("날짜와 시간 id가 모두 없으면 예외가 발생한다.")
        void 실패3() {
            List<ErrorDetail> expectedErrors = List.of(
                ErrorDetail.of("date", "date가 누락되었습니다."),
                ErrorDetail.of("timeId", "timeId가 누락되었습니다."));

            ExceptionAssertions.assertErrorCodeWithErrors(
                () -> ReservationUpdateRequestValidator.validate(null, null),
                ErrorCode.COMMON_INVALID_REQUEST_BODY,
                expectedErrors
            );
        }
    }
}
