package roomescape.controller;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import roomescape.dto.exception.ErrorResponse;

@RestControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIllegalArgumentException(HttpServletRequest request, IllegalArgumentException e) {
        return handleRuntimeExceptionWith(request, e);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIllegalStateException(HttpServletRequest request, IllegalStateException e) {
        return handleRuntimeExceptionWith(request, e);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleInvalidFormatException(HttpServletRequest request, InvalidFormatException e) {
        if (e.getMessage().contains("시간")) {
            return new ErrorResponse(request.getRequestURI(), request.getMethod(), "시간이 입력되지 않았습니다.");
        }
        if (e.getMessage().contains("테마")) {
            return new ErrorResponse(request.getRequestURI(), request.getMethod(), "테마가 입력되지 않았습니다.");
        }
        return handleRuntimeExceptionWith(request, e);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleNullPointerException(HttpServletRequest request, NullPointerException e) {
        return handleRuntimeExceptionWith(request, e);
    }

    private ErrorResponse handleRuntimeExceptionWith(HttpServletRequest request, Exception e) {
        return new ErrorResponse(request.getRequestURI(), request.getMethod(),
                e.getMessage());
    }
}
