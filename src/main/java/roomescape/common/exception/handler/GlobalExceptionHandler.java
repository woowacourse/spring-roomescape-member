package roomescape.common.exception.handler;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import roomescape.common.exception.handler.dto.ExceptionResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ExceptionResponse> handleNull(final NullPointerException exception, final HttpServletRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                500, "[ERROR] " + exception.getMessage(), request.getRequestURI()
        );

        return ResponseEntity.internalServerError().body(exceptionResponse);
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<ExceptionResponse> illegalArgument(final IllegalArgumentException exception, final HttpServletRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                400, "[ERROR] " + exception.getMessage(), request.getRequestURI()
        );

        return ResponseEntity.badRequest().body(exceptionResponse);
    }

    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public ResponseEntity<ExceptionResponse> notReadable(
            final HttpMessageNotReadableException exception, final HttpServletRequest request
    ) {
        Throwable rootCause = exception.getRootCause();
        if (rootCause instanceof IllegalArgumentException) {
            ExceptionResponse exceptionResponse = new ExceptionResponse(
                    400, "[ERROR] " + rootCause.getMessage(), request.getRequestURI()
            );
            return ResponseEntity.badRequest().body(exceptionResponse);
        }

        ExceptionResponse exceptionResponse = new ExceptionResponse(
                400, "[ERROR] 요청 입력이 잘못되었습니다.", request.getRequestURI()
        );
        return ResponseEntity.badRequest().body(exceptionResponse);
    }
}
