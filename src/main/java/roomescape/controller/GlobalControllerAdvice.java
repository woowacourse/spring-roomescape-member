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
    public ErrorResponse handleHttpMessageNotReadableException(HttpServletRequest request,
                                                               InvalidFormatException e) {
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
