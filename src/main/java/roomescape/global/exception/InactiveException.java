package roomescape.global.exception;

import org.springframework.http.HttpStatus;

public class InactiveException extends CustomException {

    public InactiveException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
