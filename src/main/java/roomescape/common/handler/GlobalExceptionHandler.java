package roomescape.common.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import roomescape.common.dto.ErrorInformation;
import roomescape.common.exception.ConflictException;
import roomescape.common.exception.NotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorInformation> handleHttpMessageNotReadable(HttpMessageNotReadableException e) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        ErrorInformation errorInformation = ErrorInformation.of(httpStatus, e.getMessage());
        return ResponseEntity.status(httpStatus)
                .body(errorInformation);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorInformation> handleIllegalArgumentException(IllegalArgumentException e) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        ErrorInformation errorInformation = ErrorInformation.of(httpStatus, e.getMessage());
        return ResponseEntity.status(httpStatus)
                .body(errorInformation);
    }

//    @ExceptionHandler(RequestValidationException.class)
//    public ResponseEntity<ErrorInformation> handleRequestValidationException(RequestValidationException e) {
//        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
//        ErrorInformation errorInformation = ErrorInformation.of(httpStatus, e.getMessage());
//        return ResponseEntity.status(httpStatus)
//                .body(errorInformation);
//    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorInformation> handleNotFound(NotFoundException e) {
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        ErrorInformation errorInformation = ErrorInformation.of(httpStatus, e.getMessage());
        return ResponseEntity.status(httpStatus)
                .body(errorInformation);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorInformation> handleConflict(ConflictException e) {
        HttpStatus httpStatus = HttpStatus.CONFLICT;
        ErrorInformation errorInformation = ErrorInformation.of(httpStatus, e.getMessage());
        return ResponseEntity.status(httpStatus)
                .body(errorInformation);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorInformation> handleUnknownException(Exception e) {
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        ErrorInformation errorInformation = ErrorInformation.of(httpStatus, e.getMessage());
        return ResponseEntity.status(httpStatus)
                .body(errorInformation);
    }

}
