package roomescape.exception;

import org.springframework.http.HttpStatus;

public class ExistsException extends CustomException {

    ExistsException(final String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }

    public static ExistsException of(final String message) {
        return new ExistsException(String.format("%s 이(가) 존재합니다.", message));
    }
}
