package roomescape.common.exceptionHandler;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import roomescape.common.exceptionHandler.dto.ExceptionResponse;

@ControllerAdvice
public class GlobalExceptionHandler {

    // TODO: IllegalArgumentException 예외 못잡는 문제 해결하기
    // instanceof 쓰면 안될까?
    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<ExceptionResponse> illegalArgument(
            final IllegalArgumentException exception, final HttpServletRequest request
    ) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                400, "[ERROR] " + exception.getMessage(), request.getRequestURI()
        );
        return ResponseEntity.badRequest().body(exceptionResponse);
    }

    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public ResponseEntity<ExceptionResponse> notReadable(final HttpServletRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                400, "[ERROR] 요청 입력이 잘못되었습니다.", request.getRequestURI()
        );
        return ResponseEntity.badRequest().body(exceptionResponse);
    }
}
