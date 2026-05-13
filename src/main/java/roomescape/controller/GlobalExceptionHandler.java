package roomescape.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import roomescape.controller.dto.ErrorResponse;
import roomescape.exception.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            InvalidInputException.class,
            PastReservationException.class})
    public ResponseEntity<ErrorResponse> handleBadRequest(RoomescapeException e) {
        return ResponseEntity.badRequest().body(ErrorResponse.from(e));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(NotFoundException e) {
        return ResponseEntity.status(404).body(ErrorResponse.from(e));
    }

    @ExceptionHandler({
            DuplicateReservationException.class,
            ResourceInUseException.class})
    public ResponseEntity<ErrorResponse> handleConflict(RoomescapeException e) {
        return ResponseEntity.status(409).body(ErrorResponse.from(e));
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoResource() {
        return ResponseEntity.status(404)
                .body(new ErrorResponse(ErrorCode.NOT_FOUND.name(), "존재하지 않는 리소스입니다."));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        return ResponseEntity.internalServerError()
                .body(new ErrorResponse(ErrorCode.INTERNAL_SERVER_ERROR.name(), "서버에 문제가 발생했습니다."));
    }
}
