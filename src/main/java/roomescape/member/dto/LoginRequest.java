package roomescape.member.dto;

import roomescape.global.exception.RoomEscapeException;

import java.util.Objects;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static roomescape.global.exception.ExceptionMessage.EMAIL_CANNOT_NULL;
import static roomescape.global.exception.ExceptionMessage.PASSWORD_CANNOT_NULL;

public record LoginRequest(
        String email,
        String password
) {
    public LoginRequest {
        try {
            Objects.requireNonNull(email, EMAIL_CANNOT_NULL.getMessage());
            Objects.requireNonNull(password, PASSWORD_CANNOT_NULL.getMessage());
        } catch (NullPointerException e) {
            throw new RoomEscapeException(BAD_REQUEST, e.getMessage());
        }
    }
}
