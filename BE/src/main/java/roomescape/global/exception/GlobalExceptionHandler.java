package roomescape.global.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import roomescape.global.exception.customException.BusinessException;
import roomescape.global.exception.customException.NotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final String INTERNAL_SERVER_ERROR_MESSAGE = "서버 내부 오류 발생\n 관리자에게 문의해주세요";

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(NoResourceFoundException.class) // 요청 경로 없음
    public ResponseEntity<ErrorResponseBody> handleResourceNotFound(NoResourceFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponseBody(ErrorType.SERVER, "잘못된 경로입니다"));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class) // HTTP 메서드 존재 X
    public ResponseEntity<ErrorResponseBody> handleHttpRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException e) {
        return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(new ErrorResponseBody(ErrorType.BUSINESS, ErrorCode.METHOD_NOT_ALLOWED.getMessage()));
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class) // 미디어 타입 X
    public ResponseEntity<ErrorResponseBody> handleHttpMediaTypeNotSupportedException(
            HttpMediaTypeNotSupportedException e) {
        return ResponseEntity
                .status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                .body(new ErrorResponseBody(ErrorType.BUSINESS, ErrorCode.INVALID_HTTP_MESSAGE.getMessage()));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class) // URL 경로 변수, 타입 안맞음
    public ResponseEntity<ErrorResponseBody> handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponseBody(ErrorType.BUSINESS, ErrorCode.INVALID_REQUEST_FORMAT.getMessage()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class) // JSON 파싱 실패
    public ResponseEntity<ErrorResponseBody> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        if (e.getRootCause() instanceof BusinessException businessException) {
            return handleBusinessException(businessException); //DTO 생성 실패
        }
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponseBody(ErrorType.BUSINESS, ErrorCode.INVALID_HTTP_MESSAGE.getMessage()));
    }

    @ExceptionHandler(NotFoundException.class) // NotFound - 비즈니스 로직에서
    public ResponseEntity<ErrorResponseBody> handleBusinessNotFoundException(NotFoundException e) {
        return ResponseEntity
                .status(e.httpStatus())
                .body(new ErrorResponseBody(ErrorType.BUSINESS, e.getMessage()));
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponseBody> handleBusinessException(BusinessException e) {
        return ResponseEntity
                .status(e.httpStatus())
                .body(new ErrorResponseBody(ErrorType.BUSINESS, e.getMessage()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponseBody> handleRuntimeException(RuntimeException e) {
        log.error("RuntimeException 발생 : ", e);
        return handleInternalServerError();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseBody> handleException(Exception e) {
        log.error("Exception 발생 : ", e);
        return handleInternalServerError();
    }

    private ResponseEntity<ErrorResponseBody> handleInternalServerError() {
        ErrorResponseBody responseBody = new ErrorResponseBody(ErrorType.SERVER, INTERNAL_SERVER_ERROR_MESSAGE);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(responseBody);
    }
}
