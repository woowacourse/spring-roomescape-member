package roomescape.application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import roomescape.exception.RoomescapeException;

public class EntityCreationException extends RoomescapeException {

    public EntityCreationException(String message) {
        super(message);
    }

    @Override
    public HttpStatusCode getStatusCode() {
        return HttpStatus.BAD_REQUEST;
    }

    @Override
    protected ProblemDetail constructBody(ProblemDetail problemDetail) {
        problemDetail.setTitle("검증 실패");
        return problemDetail;
    }
}
