package roomescape.application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import roomescape.exception.RoomescapeException;

public class EntityNotFoundException extends RoomescapeException {

    public EntityNotFoundException(String message) {
        super(message);
    }

    @Override
    public HttpStatusCode getStatusCode() {
        return HttpStatus.BAD_REQUEST;
    }

    @Override
    protected ProblemDetail constructBody(ProblemDetail problemDetail) {
        problemDetail.setTitle("찾을 수 없습니다.");
        return problemDetail;
    }
}
