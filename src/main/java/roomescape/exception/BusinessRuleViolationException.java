package roomescape.exception;

import org.springframework.http.HttpStatus;

public class BusinessRuleViolationException extends RoomeScapeClientException {
    public BusinessRuleViolationException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
