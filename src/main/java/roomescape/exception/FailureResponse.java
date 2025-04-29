package roomescape.exception;

import org.springframework.http.HttpStatus;

public record FailureResponse(HttpStatus error, String message) {
}
