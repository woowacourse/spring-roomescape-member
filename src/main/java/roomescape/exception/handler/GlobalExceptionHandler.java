package roomescape.exception.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import roomescape.exception.dto.ErrorCode;
import roomescape.exception.dto.ErrorResponse;
import roomescape.exception.dto.FieldErrorResponse;
import roomescape.exception.exception.BaseCustomException;
import roomescape.exception.exception.CustomException;

import java.util.List;

import static roomescape.exception.dto.ErrorCode.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);


    @ExceptionHandler(BaseCustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
        ErrorCode errorCode = e.getErrorCode();
        ErrorResponse errorResponse = new ErrorResponse(
                errorCode.getCode(),
                errorCode.getMessage(),
                List.of()
        );

        return ResponseEntity
                .status(errorCode.getStatus())
                .body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<FieldErrorResponse> fieldErrors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new FieldErrorResponse(
                        error.getField(),
                        error.getDefaultMessage()
                ))
                .toList();

        ErrorResponse errorResponse = new ErrorResponse(
                INVALID_INPUT.getCode(),
                INVALID_INPUT.getMessage(),
                fieldErrors
        );

        return ResponseEntity
                .status(INVALID_INPUT.getStatus())
                .body(errorResponse);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        ErrorResponse errorResponse = new ErrorResponse(
                INVALID_REQUEST_FORMAT.getCode(),
                INVALID_REQUEST_FORMAT.getMessage(),
                List.of()
        );

        return ResponseEntity
                .status(INVALID_REQUEST_FORMAT.getStatus())
                .body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpected(Exception e) {
        log.error("Unexpected error", e);

        return ResponseEntity
                .status(SERVER_ERROR.getStatus())
                .body(new ErrorResponse(
                        SERVER_ERROR.getCode(),
                        SERVER_ERROR.getMessage(),
                        List.of()
                ));
    }
}
