package roomescape.exception;

import org.springframework.http.HttpStatus;

public record ErrorResponseDto(HttpStatus status, String message) {
}
