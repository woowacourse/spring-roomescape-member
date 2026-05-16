package roomescape.exception;

import org.springframework.http.HttpStatus;

public class InvalidDomainStateException extends RoomescapeException {

    public InvalidDomainStateException(String message) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, "INVALID_DOMAIN_STATE", message);
    }
}
