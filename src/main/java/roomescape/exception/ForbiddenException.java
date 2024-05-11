package roomescape.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

public class ForbiddenException extends RuntimeException {

    private static final HttpStatus STATUS = HttpStatus.FORBIDDEN;
    private static final String PROBLEM_DETAIL_TITLE = "권한이 부족합니다.";

    private final ProblemDetail body;

    public ForbiddenException(String message) {
        super(message);
        body = ProblemDetail.forStatusAndDetail(STATUS, message);
        body.setTitle(PROBLEM_DETAIL_TITLE);
    }

    public ForbiddenException(String message, Throwable cause) {
        super(message, cause);
        body = ProblemDetail.forStatusAndDetail(STATUS, message);
        body.setTitle(PROBLEM_DETAIL_TITLE);
    }

    public ProblemDetail getBody() {
        return body;
    }

    public HttpStatus getHttpStatus() {
        return STATUS;
    }
}
