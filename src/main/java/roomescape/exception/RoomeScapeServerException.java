package roomescape.exception;

import org.springframework.http.HttpStatus;

public abstract class RoomeScapeServerException extends RoomeScapeException {
    protected RoomeScapeServerException(String message, HttpStatus status) {
        super(message, status);
    }
}
