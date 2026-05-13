package roomescape.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import roomescape.exception.code.CommonErrorCode;
import roomescape.exception.code.ErrorCode;
import roomescape.exception.response.ErrorResponse;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Handler Method
     */
    @ExceptionHandler({Exception.class})
    public ResponseEntity<ErrorResponse> handleAllExceptions(Exception e) {
        log.warn("handleAllExceptions", e);
        ErrorCode errorCode = CommonErrorCode.INTERNAL_SERVER_ERROR;
        return handleExceptionInternal(errorCode);
    }

    /**
     * handleExceptionInternal Method
     */
    /**
     * ResponseEntityExceptionHandler이 처리하는 스프링 기본 예외 응답 포멧도 통일하기 위한 재정의
     */
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            Exception ex, @Nullable Object body, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {
        return ResponseEntity.status(statusCode)
                .headers(headers)
                .body(makeErrorResponse(statusCode));
    }

    /**
     * 표준 에러 진입점
     */
    private ResponseEntity<ErrorResponse> handleExceptionInternal(
            ErrorCode errorCode, String message
    ) {
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(makeErrorResponse(errorCode, message));
    }

    /**
     * custom Exception 진입점
     */
    private ResponseEntity<ErrorResponse> handleExceptionInternal(
            ErrorCode errorCode
    ) {
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(makeErrorResponse(errorCode));
    }

    // makeErrorResponse Method

    /**
     * ResponseEntityExceptionHandler 전용 응답 포멧팅 (스프링 기본 예외 응답 생성 메서드)
     */
    private ErrorResponse makeErrorResponse(HttpStatusCode httpStatusCode) {
        return ErrorResponse.builder()
                .code(String.valueOf(httpStatusCode.value()))
                .message(resolveMessage(httpStatusCode))
                .build();
    }

    private ErrorResponse makeErrorResponse(ErrorCode errorCode) {
        return ErrorResponse.builder()
                .code(errorCode.name())
                .message(errorCode.getMessage())
                .build();
    }

    private ErrorResponse makeErrorResponse(ErrorCode errorCode, String message) {
        return ErrorResponse.builder()
                .code(errorCode.name())
                .message(message)
                .build();
    }

    /**
     * message resolver
     */
    private String resolveMessage(HttpStatusCode httpStatusCode) {
        return switch (httpStatusCode.value()) {
            case 400 -> "잘못된 요청입니다.";
            case 401 -> "인증이 필요합니다.";
            case 403 -> "접근 권한이 없습니다.";
            case 404 -> "요청한 리소스를 찾을 수 없습니다.";
            case 405 -> "지원하지 않는 HTTP 메서드입니다.";
            case 415 -> "지원하지 않는 미디어 타입입니다.";
            case 500 -> "서버 내부 오류가 발생했습니다.";
            default -> "요청 처리 중 오류가 발생했습니다.";
        };
    }
}
