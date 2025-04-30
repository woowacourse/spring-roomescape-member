package roomescape.exception;

import org.springframework.http.HttpStatus;

public class ExistedThemeException extends CustomException {

    private static final String message = "테마가 존재합니다.";
    private static final HttpStatus status = HttpStatus.BAD_REQUEST;

    public ExistedThemeException() {
        super(message, status);
    }
}
