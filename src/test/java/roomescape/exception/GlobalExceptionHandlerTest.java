package roomescape.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();

    @Test
    void 유효하지_않은_요청_예외를_bad_request로_응답한다() {
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleInvalidRequestException(
                new InvalidRequestException("잘못된 요청입니다.")
        );

        assertErrorResponse(response, HttpStatus.BAD_REQUEST, "잘못된 요청입니다.");
    }

    @Test
    void 조회_실패_예외를_not_found로_응답한다() {
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleNotFoundException(
                new NotFoundException("존재하지 않는 리소스입니다.")
        );

        assertErrorResponse(response, HttpStatus.NOT_FOUND, "존재하지 않는 리소스입니다.");
    }

    @Test
    void 충돌_예외를_conflict로_응답한다() {
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleConflictException(
                new ConflictException("이미 존재하는 리소스입니다.")
        );

        assertErrorResponse(response, HttpStatus.CONFLICT, "이미 존재하는 리소스입니다.");
    }

    @Test
    void 인프라_예외를_internal_server_error로_응답한다() {
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleInfrastructureException(
                new InfrastructureException("저장에 실패했습니다.")
        );

        assertErrorResponse(response, HttpStatus.INTERNAL_SERVER_ERROR, "저장에 실패했습니다.");
    }

    private void assertErrorResponse(ResponseEntity<ErrorResponse> response, HttpStatus httpStatus, String message) {
        assertThat(response.getStatusCode()).isEqualTo(httpStatus);
        assertThat(response.getBody()).isEqualTo(new ErrorResponse(message));
    }
}
