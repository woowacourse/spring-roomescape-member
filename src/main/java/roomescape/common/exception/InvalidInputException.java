package roomescape.common.exception;

import org.springframework.http.HttpStatus;

public class InvalidInputException extends CustomException {
    public InvalidInputException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
