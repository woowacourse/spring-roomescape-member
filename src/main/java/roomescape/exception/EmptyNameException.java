package roomescape.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;

public class EmptyNameException extends BusinessException {

    private static final String ERROR_MESSAGE = "비어 있지 않은 이름을 입력해 주세요.";

    public EmptyNameException() {
        super(ERROR_MESSAGE);
    }

    @Override
    public HttpStatusCode getStatusCode() {
        return HttpStatus.UNPROCESSABLE_ENTITY;
    }

    @Override
    public ProblemDetail getBody() {
        return ProblemDetail.forStatusAndDetail(getStatusCode(), "이름이 비어 있습니다.");
    }
}
