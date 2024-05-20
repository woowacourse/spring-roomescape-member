package roomescape.exception;

import org.springframework.http.HttpStatus;

public record ErrorResponse(HttpStatus status, String message) {

    public static ErrorResponse from(final CustomException exception) {
        return new ErrorResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }
}
