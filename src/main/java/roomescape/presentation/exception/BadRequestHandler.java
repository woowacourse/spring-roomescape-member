package roomescape.presentation.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestHandler {

    @ExceptionHandler(BadRequestException.class)
    private ErrorResponse handleBadRequestException(final BadRequestException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    private ErrorResponse handleJsonParseError() {
        return new ErrorResponse("요청 형식이 올바르지 않습니다.");
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    private ErrorResponse handleTypeMismatch() {
        return new ErrorResponse("요청 파라미터의 타입이 올바르지 않습니다.");
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    private ErrorResponse handleMissingParam(final MissingServletRequestParameterException e) {
        return new ErrorResponse(String.format("요청 파라미터 '%s'가 누락되었습니다.", e.getParameterName()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    private ErrorResponse handleMethodArgumentException(final MethodArgumentNotValidException e) {
        return new ErrorResponse(e.getBindingResult().getFieldError().getDefaultMessage());
    }
}
