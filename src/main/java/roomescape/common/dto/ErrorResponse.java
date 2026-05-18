package roomescape.common.dto;

import org.springframework.http.HttpStatus;

public record ErrorResponse(
        int status,
        String message
) {
    public static ErrorResponse of(HttpStatus httpStatus, String message) {
        return new ErrorResponse(httpStatus.value(), message);
    }
}
