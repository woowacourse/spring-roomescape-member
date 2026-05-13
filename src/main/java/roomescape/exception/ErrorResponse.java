package roomescape.exception;

import org.springframework.http.HttpStatus;

public record ErrorResponse(String code, String message, HttpStatus status) {
}
