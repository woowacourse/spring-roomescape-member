package roomescape.controller.dto;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;

public record ErrorResponse(
        int status,
        String message,
        String path
) {

    public static ErrorResponse of(HttpStatus status, String message, HttpServletRequest request) {
        return new ErrorResponse(status.value(), message, request.getRequestURI());
    }
}
