package roomescape.exception;

import org.springframework.http.HttpStatus;

public class ArgumentNullException extends CustomException {

    private static final String message = "값이 존재하지 않습니다.";
    private static final HttpStatus status = HttpStatus.NOT_FOUND;

    public ArgumentNullException() {
        super(message, status);
    }
}
