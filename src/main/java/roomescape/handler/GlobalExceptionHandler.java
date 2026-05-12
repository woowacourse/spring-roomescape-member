package roomescape.handler;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import roomescape.dto.ErrorResponse;
import roomescape.exception.ErrorCode;
import roomescape.exception.RoomescapeException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RoomescapeException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(RoomescapeException e,
                                                                     HttpServletRequest request) {

        ErrorResponse response = new ErrorResponse(e.getCode().toString(), request.getRequestURI(),
                e.getCode().getMessage(),
                e.getCode().getAction());

        return ResponseEntity.status(e.getCode().getStatus()).body(response);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException e,
                                                                               HttpServletRequest request) {
        ErrorResponse response = new ErrorResponse("INVALID_REQUEST_BODY", request.getRequestURI(),
                "입력 형식이 잘못되었습니다.", "입력 형식을 확인하세요.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllException(Exception e, HttpServletRequest request) {
        ErrorCode errorCode = ErrorCode.INTERNAL_ERROR;
        ErrorResponse response = new ErrorResponse(errorCode.toString(), request.getRequestURI(),
                errorCode.getMessage(), errorCode.getAction());
        return ResponseEntity.status(ErrorCode.INTERNAL_ERROR.getStatus()).body(response);
    }
}
