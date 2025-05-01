package roomescape.common.exceptionHandler;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import roomescape.common.exceptionHandler.dto.ExceptionResponse;

@ControllerAdvice
public class GlobalExceptionHandler {

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
