package roomescape.controller;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.servlet.http.HttpServletRequest;
import java.util.stream.Collectors;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import roomescape.dto.exception.ErrorResponse;

@RestControllerAdvice
public class GlobalControllerAdvice {

    /*
     * Request DTO 에서의 @NotEmpty 와 같은 어노테이션에 대한 예외 처리
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentNotValidException(HttpServletRequest request,
                                                               MethodArgumentNotValidException e) {
        String messages = e.getBindingResult().getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));

        return new ErrorResponse(request.getRequestURI(), request.getMethod(), messages);
    }

    /*
     * 예약을 추가할 때 시간과 테마가 입력되지 않은 경우에 대한 예외 처리
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleInvalidFormatException(HttpServletRequest request, InvalidFormatException e) {
        String message = e.getMessage();
        if (message.contains("멤버")) {
            message = "멤버가 입력되지 않았습니다.";
        }
        if (message.contains("시간")) {
            message = "시간이 입력되지 않았습니다.";
        }
        if (message.contains("테마")) {
            message = "테마가 입력되지 않았습니다.";
        }
        return createErrorResponse(request, message);
    }

    @ExceptionHandler(value = {IllegalArgumentException.class, IllegalStateException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIllegalException(HttpServletRequest request, RuntimeException e) {
        return createErrorResponse(request, e.getMessage());
    }

    private ErrorResponse createErrorResponse(HttpServletRequest request, String message) {
        return new ErrorResponse(request.getRequestURI(), request.getMethod(), message);
    }
}
