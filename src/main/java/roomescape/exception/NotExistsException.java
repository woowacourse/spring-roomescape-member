package roomescape.exception;

import org.springframework.http.HttpStatus;

public class NotExistsException extends CustomException {

    public NotExistsException(final String message) {
        super(HttpStatus.NOT_FOUND, message);
    }

    public static NotExistsException of(final String target, final String input) {
        return new NotExistsException(String.format("%s 이(가) 존재하지 않습니다. (input = %s)", target, input));
    }

    public static NotExistsException of(final String target, final long input) {
        return NotExistsException.of(target, String.valueOf(input));
    }
}
