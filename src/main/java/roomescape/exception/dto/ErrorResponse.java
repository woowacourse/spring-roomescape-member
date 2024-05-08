package roomescape.exception.dto;

import org.springframework.http.HttpStatus;

public record ErrorResponse(HttpStatus code, String detail) {
}
