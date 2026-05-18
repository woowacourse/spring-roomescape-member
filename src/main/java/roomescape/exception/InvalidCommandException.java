package roomescape.exception;

import org.springframework.http.HttpStatus;

public class InvalidCommandException extends RoomeScapeClientException {
    public InvalidCommandException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
