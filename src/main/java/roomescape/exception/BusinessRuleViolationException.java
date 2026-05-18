package roomescape.exception;

import org.springframework.http.HttpStatus;

public class BusinessRuleViolationException extends RoomescapeException {

    public BusinessRuleViolationException(String message) {
        super(HttpStatus.UNPROCESSABLE_ENTITY, "BUSINESS_RULE_VIOLATION", message);
    }
}
