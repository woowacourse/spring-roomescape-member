package roomescape.domain.reservation.dto.request;

import static org.assertj.core.api.Assertions.assertThatCode;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import roomescape.domain.global.exception.error.ErrorCode;
import roomescape.domain.global.exception.error.ErrorDetail;
import roomescape.global.ExceptionAssertions;

class ReservationUpdateRequestDtoTest {

    @Nested
    @DisplayName("생성 테스트")
    class Constructor {

        @Test
        @DisplayName("정상 테스트")
        void 성공() {
            assertThatCode(() -> new ReservationUpdateRequestDto(LocalDate.of(2026, 5, 1), 1L))
                .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("날짜가 누락된 경우 예외가 발생한다.")
        void 실패1() {
            List<ErrorDetail> expectedErrors = List.of(
                ErrorDetail.of("date", "date가 누락되었습니다."));

            ExceptionAssertions.assertErrorCodeWithErrors(
                () -> new ReservationUpdateRequestDto(null, 1L),
                ErrorCode.COMMON_INVALID_REQUEST_BODY,
                expectedErrors
            );
        }

        @Test
        @DisplayName("시간 id가 누락된 경우 예외가 발생한다.")
        void 실패2() {
            List<ErrorDetail> expectedErrors = List.of(
                ErrorDetail.of("timeId", "timeId가 누락되었습니다."));

            ExceptionAssertions.assertErrorCodeWithErrors(
                () -> new ReservationUpdateRequestDto(LocalDate.of(2026, 5, 1), null),
                ErrorCode.COMMON_INVALID_REQUEST_BODY,
                expectedErrors
            );
        }
    }
}
