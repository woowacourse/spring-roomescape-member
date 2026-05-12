package roomescape.global.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.MDC;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException illegalArgumentException) {
        return ResponseEntity.badRequest().body(illegalArgumentException.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(
            CustomException customException, HttpServletRequest httpServletRequest
    ) {
        ErrorResponse errorResponse = new ErrorResponseBuilder()
                .httpStatus(customException.getErrorCode().getHttpStatus())
                .errorMessage(customException.getErrorCode().getMessage())
                .apiUrl(httpServletRequest.getRequestURI())
                .timeStamp(LocalDateTime.now())
                .traceId(MDC.get("traceId"))
                .build();

        return ResponseEntity.status(customException.getHttpStatus()).body(errorResponse);
    }
}
