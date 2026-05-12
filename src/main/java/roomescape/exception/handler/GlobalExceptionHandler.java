package roomescape.exception.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import roomescape.exception.*;
import roomescape.exception.dto.ErrorResponse;
import roomescape.exception.dto.FieldErrorResponse;

import java.util.List;

import static roomescape.exception.ErrorCode.*;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({
            NotFoundResourceException.class,
            DataReferencedException.class,
            ReservationCommandException.class,
            ReservationTimeConditionException.class,
            DuplicatedReservationRequestException.class
    })
    public ResponseEntity<String> handleCustomException(CustomException customException) {
        return new ResponseEntity<>(customException.getMessage(), HttpErrorMapping.getHttpStatus(customException.getErrorMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException e) {

        List<FieldErrorResponse> fieldErrors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new FieldErrorResponse(
                        error.getField(),
                        error.getDefaultMessage()
                ))
                .toList();

        ErrorResponse response = new ErrorResponse(
                VALIDATION_ERROR.getCode(),
                VALIDATION_ERROR.getMessage(),
                fieldErrors
        );

        return ResponseEntity
                .badRequest()
                .body(response);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleFormat(HttpMessageNotReadableException e) {

        return ResponseEntity.badRequest().body(
                new ErrorResponse(
                        INVALID_REQUEST_FORMAT.getCode(),
                        INVALID_REQUEST_FORMAT.getMessage(),
                        List.of()
                )
        );
    }
}
