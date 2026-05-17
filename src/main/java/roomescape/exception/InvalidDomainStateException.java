package roomescape.exception;

import org.springframework.http.HttpStatus;

public class InvalidDomainStateException extends RoomescapeException {

    public InvalidDomainStateException(String message) {
        super(HttpStatus.BAD_REQUEST, "INVALID_DOMAIN_STATE", message);
    }
}
