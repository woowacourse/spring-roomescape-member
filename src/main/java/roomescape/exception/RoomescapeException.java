package roomescape.exception;

import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponse;

public abstract class RoomescapeException extends RuntimeException implements ErrorResponse {

    protected RoomescapeException(String message) {
        super(message);
    }

    protected abstract ProblemDetail constructBody(ProblemDetail problemDetail);

    @Override
    public ProblemDetail getBody() {
        ProblemDetail problemDetail = ProblemDetail.forStatus(getStatusCode());
        problemDetail.setDetail(getMessage());
        return constructBody(problemDetail);
    }
}
