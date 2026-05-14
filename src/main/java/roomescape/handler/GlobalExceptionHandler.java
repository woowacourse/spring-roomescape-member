package roomescape.handler;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import roomescape.dto.ErrorResponse;
import roomescape.exception.ErrorCode;
import roomescape.exception.RoomescapeException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ErrorResponse>> handleIllegalArgumentException(MethodArgumentNotValidException e,
                                                                              HttpServletRequest request) {
        List<ErrorResponse> errors = new ArrayList<>();
        e.getBindingResult().getFieldErrors().forEach((fieldError) -> {
            ErrorCode error = ErrorCode.valueOf(fieldError.getDefaultMessage());
            ErrorResponse errorResponse = new ErrorResponse(
                    error.getCode(), request.getRequestURI(), error.getMessage(), error.getAction()
            );
            errors.add(errorResponse);
        });
        return ResponseEntity.status(e.getStatusCode()).body(errors);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException e,
                                                                               HttpServletRequest request) {
        ErrorResponse errorResponse = new ErrorResponse("INVALID_REQUEST", request.getRequestURI(),
                "요청 본문의 형식이 올바르지 않습니다.", "JSON 형식과 필드 타입을 확인해주세요.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingServletRequestParameterException(
            MissingServletRequestParameterException e, HttpServletRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                "INVALID_PARAMETER", request.getRequestURI(),
                "필수 요청 파라미터가 누락되었습니다.", "요청 파라미터를 확인해주세요."
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(RoomescapeException.class)
    public ResponseEntity<ErrorResponse> handleRoomescapeException(RoomescapeException e, HttpServletRequest request) {
        ErrorCode error = e.getErrorCode();
        ErrorResponse errorResponse = new ErrorResponse(
                error.getCode(), request.getRequestURI(), error.getMessage(), error.getAction()
        );
        return ResponseEntity.status(error.getStatus()).body(errorResponse);
    }
}
