package roomescape.controller.advice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import roomescape.exception.AuthorizationException;
import roomescape.exception.BadRequestException;
import roomescape.exception.NotFoundException;

@RestControllerAdvice
public class ControllerExceptionHandler {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<ProblemDetail> handleAuthorizationException(final AuthorizationException exception) {
        log.error("[Authorization Exception]", exception);
        return createErrorResponse(
                exception.getStatus(),
                exception.getMessage(),
                "Authorization Exception"
        );
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ProblemDetail> handleBadRequestException(final BadRequestException exception) {
        log.error("[Bad Request Exception]", exception);
        return createErrorResponse(
                exception.getStatus(),
                exception.getMessage(),
                "Bad Request Exception"
        );
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ProblemDetail> handleNotFoundException(final NotFoundException exception) {
        log.error("[Not Found Exception]", exception);
        return createErrorResponse(
                exception.getStatus(),
                exception.getMessage(),
                "Not Found Exception"
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleMethodArgumentNotValidException(
            final MethodArgumentNotValidException exception
    ) {
        log.error("[Method Argument Not Valid Exception]", exception);
        final String message = exception.getBindingResult()
                .getAllErrors()
                .get(0)
                .getDefaultMessage();

        return createErrorResponse(
                HttpStatus.BAD_REQUEST,
                message,
                "Method Argument Not Valid Exception"
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ProblemDetail> handleIllegalArgumentException(final IllegalArgumentException exception) {
        log.error("[Illegal Argument Exception]", exception);
        return createErrorResponse(
                HttpStatus.BAD_REQUEST,
                "잘못된 파라미터 요청입니다.",
                "Illegal Argument Exception"
        );
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ProblemDetail> handleDataAccessException(final DataAccessException exception) {
        log.error("[Data Access Exception]", exception);
        return createErrorResponse(
                HttpStatus.BAD_REQUEST,
                "잘못된 DB 접근입니다.",
                "Illegal Argument Exception"
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleServerException(final Exception exception) {
        log.error("[Exception]", exception);
        return createErrorResponse(
                HttpStatus.BAD_REQUEST,
                "서버 에러입니다.",
                "Exception"
        );
    }

    private ResponseEntity<ProblemDetail> createErrorResponse(
            final HttpStatus httpStatus,
            final String errorMessage,
            final String errorTitle
    ) {
        final ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(httpStatus, errorMessage);
        problemDetail.setTitle(errorTitle);
        return new ResponseEntity<>(problemDetail, httpStatus);
    }
}
