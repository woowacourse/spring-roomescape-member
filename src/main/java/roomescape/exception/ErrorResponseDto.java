package roomescape.exception;

import org.springframework.http.HttpStatus;

public record ErrorResponseDto(HttpStatus status, String message) {
    public static ErrorResponseDto of(HttpStatus status, String message) {
        return new ErrorResponseDto(status, message);
    }
}
