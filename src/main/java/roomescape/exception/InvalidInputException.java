package roomescape.exception;

import org.springframework.http.HttpStatus;

public class InvalidInputException extends CustomException {

    public InvalidInputException(final String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }

    public static InvalidInputException of(final String target, final String input) {
        return new InvalidInputException(String.format("%s 이(가) 유효하지 않습니다. (input = %s)", target, input));
    }

    public static InvalidInputException of(final String target, final int input) {
        return InvalidInputException.of(target, String.valueOf(input));
    }
}
