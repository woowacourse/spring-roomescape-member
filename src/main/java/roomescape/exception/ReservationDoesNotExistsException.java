package roomescape.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;

public class ReservationDoesNotExistsException extends BusinessException {

    private static final String ERROR_MESSAGE = "예약이 존재하지 않습니다.";

    public ReservationDoesNotExistsException() {
        super(ERROR_MESSAGE);
    }

    @Override
    public HttpStatusCode getStatusCode() {
        return HttpStatus.NOT_FOUND;
    }

    @Override
    public ProblemDetail getBody() {
        return ProblemDetail.forStatusAndDetail(getStatusCode(), "예약이 존재하지 않습니다.");
    }
}
