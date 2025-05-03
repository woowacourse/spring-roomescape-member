package roomescape.handler;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import roomescape.exception.CustomException;
import roomescape.exception.ErrorCode;
import roomescape.exception.ErrorResponse;
import roomescape.exception.custom.DuplicatedException;
import roomescape.exception.custom.InvalidInputException;
import roomescape.exception.custom.NotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {
        InvalidInputException.class,
        NotFoundException.class,
        DuplicatedException.class
    })
    public ResponseEntity<ErrorResponse> handleInvalidInput(CustomException e) {
        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity
            .status(errorCode.getHttpStatus())
            .body(new ErrorResponse(errorCode.name(), e.getMessage()));
    }

    @ExceptionHandler(value = DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDeleteConflict(DataIntegrityViolationException e) {
        ErrorCode errorCode = ErrorCode.DELETE_CONFLICT;
        return ResponseEntity
            .status(errorCode.getHttpStatus())
            .body(new ErrorResponse(errorCode.name(), e.getMessage()));
    }
}
