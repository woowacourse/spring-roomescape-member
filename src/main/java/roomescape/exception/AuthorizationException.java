package roomescape.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

public class AuthorizationException extends RuntimeException {

    private static final HttpStatus STATUS = HttpStatus.UNAUTHORIZED;
    private static final String PROBLEM_DETAIL_TITLE = "유효하지 않은 인증 정보입니다.";

    private final ProblemDetail body;

    public AuthorizationException(String message) {
        super(message);
        body = ProblemDetail.forStatusAndDetail(STATUS, message);
        body.setTitle(PROBLEM_DETAIL_TITLE);
    }

    public AuthorizationException(String message, Throwable cause) {
        super(message, cause);
        body = ProblemDetail.forStatusAndDetail(STATUS, message);
        body.setTitle(PROBLEM_DETAIL_TITLE);
    }

    public ProblemDetail getBody() {
        return body;
    }
}
