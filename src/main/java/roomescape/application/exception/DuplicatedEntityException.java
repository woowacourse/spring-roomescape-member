package roomescape.application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import roomescape.exception.RoomescapeException;

public class DuplicatedEntityException extends RoomescapeException {

    public DuplicatedEntityException(String message) {
        super(message);
    }

    @Override
    public HttpStatusCode getStatusCode() {
        return HttpStatus.CONFLICT;
    }

    @Override
    protected ProblemDetail constructBody(ProblemDetail problemDetail) {
        problemDetail.setTitle("중복 검증 실패");
        return problemDetail;
    }
}
