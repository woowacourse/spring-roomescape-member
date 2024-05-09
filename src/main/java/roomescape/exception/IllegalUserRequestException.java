package roomescape.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

public class IllegalUserRequestException extends RuntimeException {

    private static final HttpStatus STATUS = HttpStatus.BAD_REQUEST;
    private static final String PROBLEM_DETAIL_TITLE = "유효하지 않은 요청 데이터입니다.";

    private final ProblemDetail body;

    public IllegalUserRequestException(String message) {
        super(message);
        body = ProblemDetail.forStatusAndDetail(STATUS, message);
        body.setTitle(PROBLEM_DETAIL_TITLE);
    }

    public IllegalUserRequestException(String message, Throwable cause) {
        super(message, cause);
        body = ProblemDetail.forStatusAndDetail(STATUS, message);
        body.setTitle(PROBLEM_DETAIL_TITLE);
    }

    public ProblemDetail getBody() {
        return body;
    }
}
