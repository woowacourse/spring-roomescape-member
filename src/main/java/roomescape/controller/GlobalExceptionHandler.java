package roomescape.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import roomescape.controller.dto.ErrorResponse;
import roomescape.exception.DuplicateReservationException;
import roomescape.exception.ErrorCode;
import roomescape.exception.InvalidInputException;
import roomescape.exception.NotFoundException;
import roomescape.exception.PastReservationException;
import roomescape.exception.RoomescapeException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            InvalidInputException.class,
            PastReservationException.class
    })
    public ResponseEntity<ErrorResponse> handleBadRequest(RoomescapeException e) {
        return ResponseEntity.badRequest().body(ErrorResponse.from(e));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(NotFoundException e) {
        return ResponseEntity.status(404).body(ErrorResponse.from(e));
    }

    @ExceptionHandler(DuplicateReservationException.class)
    public ResponseEntity<ErrorResponse> handleConflict(RoomescapeException e) {
        return ResponseEntity.status(409).body(ErrorResponse.from(e));
    }

    @ExceptionHandler({
            HttpMessageNotReadableException.class,
            MethodArgumentTypeMismatchException.class
    })
    public ResponseEntity<ErrorResponse> handleInvalidDateTimeFormat() {
        return ResponseEntity.badRequest()
                .body(new ErrorResponse(ErrorCode.INVALID_INPUT.name(), "날짜 또는 시간 형식이 올바르지 않습니다."));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingRequestParameter() {
        return ResponseEntity.badRequest()
                .body(new ErrorResponse(ErrorCode.INVALID_INPUT.name(), "필수 요청값이 누락되었습니다."));
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoResource() {
        return ResponseEntity.status(404)
                .body(new ErrorResponse(ErrorCode.NOT_FOUND.name(), "존재하지 않는 리소스입니다."));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        return ResponseEntity.internalServerError().body("[ERROR] 서버에 문제가 발생했습니다.");
    }
}
