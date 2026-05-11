package roomescape.support.exception;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

class GlobalExceptionHandlerTest {

    @Test
    void RoomescapeException을_에러_응답으로_변환한다() {
        // given
        GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();
        RoomescapeException exception = new RoomescapeException(ReservationErrorCode.INVALID_RESERVATION_NAME);

        // when
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleRoomescapeException(exception);

        // then
        assertSoftly(softly -> {
            softly.assertThat(response.getStatusCode().value()).isEqualTo(400);
            softly.assertThat(response.getBody().code()).isEqualTo("INVALID_RESERVATION_NAME");
            softly.assertThat(response.getBody().message()).isEqualTo("이름은 비어 있을 수 없습니다.");
        });
    }

    @Test
    void 예상하지_못한_예외를_500_에러_응답으로_변환한다() {
        // given
        GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();
        Exception exception = new IllegalStateException();

        // when
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleException(exception);

        // then
        assertSoftly(softly -> {
            softly.assertThat(response.getStatusCode().value()).isEqualTo(500);
            softly.assertThat(response.getBody().code()).isEqualTo("INTERNAL_SERVER_ERROR");
            softly.assertThat(response.getBody().message()).isEqualTo("서버 내부 오류가 발생했습니다");
        });
    }
}
