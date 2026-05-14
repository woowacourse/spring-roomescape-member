package roomescape.global.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import roomescape.global.exception.customException.BusinessException;
import roomescape.global.exception.customException.NotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final String INTERNAL_SERVER_ERROR_MESSAGE = "서버 내부 오류 발생\n 관리자에게 문의해주세요";

    // 404 NotFound - BusinessException
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponseBody> handleBusinessNotFoundException(NotFoundException e) {
        new ErrorResponseBody(ErrorType.BUSINESS, e.getMessage());
        return ResponseEntity
                .status(e.httpStatus())
                .body(new ErrorResponseBody(ErrorType.BUSINESS, e.getMessage()));
    }

    // 404 NotFound - ServerException
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponseBody> handleResourceNotFound(NoResourceFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponseBody(ErrorType.SERVER, "잘못된 경로입니다"));
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponseBody> handleBusinessException(BusinessException e) {
        return ResponseEntity
                .status(e.httpStatus())
                .body(new ErrorResponseBody(ErrorType.BUSINESS, e.getMessage()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponseBody> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponseBody(ErrorType.BUSINESS, ErrorCode.INVALID_REQUEST_FORMAT.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseBody> handleException(Exception e) {
        ErrorResponseBody errorResponseBody = new ErrorResponseBody(ErrorType.SERVER, INTERNAL_SERVER_ERROR_MESSAGE);
        return ResponseEntity
                .status(500)
                .body(errorResponseBody);
    }
}
