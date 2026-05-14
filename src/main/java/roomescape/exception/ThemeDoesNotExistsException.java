package roomescape.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;

public class ThemeDoesNotExistsException extends BusinessException {

    private static final String ERROR_MESSAGE = "테마가 존재하지 않습니다.";

    public ThemeDoesNotExistsException() {
        super(ERROR_MESSAGE);
    }

    @Override
    public HttpStatusCode getStatusCode() {
        return HttpStatus.NOT_FOUND;
    }

    @Override
    public ProblemDetail getBody() {
        return ProblemDetail.forStatusAndDetail(getStatusCode(), "존재하지 않는 테마입니다.");
    }
}
