package roomescape.controller.dto;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.controller.exception.InvalidRequestException;

class ReservationRequestTest {

    private static final String VALID_NAME = "브라운";
    private static final LocalDate VALID_DATE = LocalDate.of(2026, 5, 9);
    private static final Long VALID_TIME_ID = 1L;
    private static final Long VALID_THEME_ID = 1L;

    @Test
    @DisplayName("모든 값이 유효하면 요청을 생성한다")
    void 모든_값이_유효하면_요청을_생성한다() {
        assertDoesNotThrow(() -> new ReservationRequest(VALID_NAME, VALID_DATE, VALID_TIME_ID, VALID_THEME_ID));
    }

    @Test
    @DisplayName("이름이 null이면 예외가 발생한다")
    void 이름이_null이면_예외가_발생한다() {
        InvalidRequestException exception = assertThrows(
                InvalidRequestException.class,
                () -> new ReservationRequest(null, VALID_DATE, VALID_TIME_ID, VALID_THEME_ID)
        );
        assertEquals("예약자 이름은 필수입니다", exception.getMessage());
    }

    @Test
    @DisplayName("이름이 빈문자열이면 예외가 발생한다")
    void 이름이_빈문자열이면_예외가_발생한다() {
        assertThrows(
                InvalidRequestException.class,
                () -> new ReservationRequest("", VALID_DATE, VALID_TIME_ID, VALID_THEME_ID)
        );
    }

    @Test
    @DisplayName("이름이 공백만으로 이루어져 있으면 예외가 발생한다")
    void 이름이_공백만으로_이루어져_있으면_예외가_발생한다() {
        assertThrows(
                InvalidRequestException.class,
                () -> new ReservationRequest("   ", VALID_DATE, VALID_TIME_ID, VALID_THEME_ID)
        );
    }

    @Test
    @DisplayName("날짜가 null이면 예외가 발생한다")
    void 날짜가_null이면_예외가_발생한다() {
        InvalidRequestException exception = assertThrows(
                InvalidRequestException.class,
                () -> new ReservationRequest(VALID_NAME, null, VALID_TIME_ID, VALID_THEME_ID)
        );
        assertEquals("예약 날짜는 필수입니다", exception.getMessage());
    }

    @Test
    @DisplayName("시간 ID가 null이면 예외가 발생한다")
    void 시간_ID가_null이면_예외가_발생한다() {
        InvalidRequestException exception = assertThrows(
                InvalidRequestException.class,
                () -> new ReservationRequest(VALID_NAME, VALID_DATE, null, VALID_THEME_ID)
        );
        assertEquals("시간 ID는 양수여야 합니다", exception.getMessage());
    }

    @Test
    @DisplayName("시간 ID가 0이면 예외가 발생한다")
    void 시간_ID가_0이면_예외가_발생한다() {
        assertThrows(
                InvalidRequestException.class,
                () -> new ReservationRequest(VALID_NAME, VALID_DATE, 0L, VALID_THEME_ID)
        );
    }

    @Test
    @DisplayName("시간 ID가 음수이면 예외가 발생한다")
    void 시간_ID가_음수이면_예외가_발생한다() {
        assertThrows(
                InvalidRequestException.class,
                () -> new ReservationRequest(VALID_NAME, VALID_DATE, -1L, VALID_THEME_ID)
        );
    }

    @Test
    @DisplayName("테마 ID가 null이면 예외가 발생한다")
    void 테마_ID가_null이면_예외가_발생한다() {
        InvalidRequestException exception = assertThrows(
                InvalidRequestException.class,
                () -> new ReservationRequest(VALID_NAME, VALID_DATE, VALID_TIME_ID, null)
        );
        assertEquals("테마 ID는 양수여야 합니다", exception.getMessage());
    }

    @Test
    @DisplayName("테마 ID가 0이면 예외가 발생한다")
    void 테마_ID가_0이면_예외가_발생한다() {
        assertThrows(
                InvalidRequestException.class,
                () -> new ReservationRequest(VALID_NAME, VALID_DATE, VALID_TIME_ID, 0L)
        );
    }

    @Test
    @DisplayName("테마 ID가 음수이면 예외가 발생한다")
    void 테마_ID가_음수이면_예외가_발생한다() {
        assertThrows(
                InvalidRequestException.class,
                () -> new ReservationRequest(VALID_NAME, VALID_DATE, VALID_TIME_ID, -1L)
        );
    }
}
