package roomescape.global.exception.exception;

import lombok.Getter;

@Getter
public class InvalidUserInputException extends RuntimeException {
    public InvalidUserInputException(String message) {
        super(message);
    }
}
