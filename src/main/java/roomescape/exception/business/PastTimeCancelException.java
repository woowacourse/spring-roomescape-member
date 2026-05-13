package roomescape.exception.business;

import org.springframework.http.HttpStatus;

public class PastTimeCancelException extends BusinessException {

    public PastTimeCancelException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}