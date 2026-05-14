package roomescape.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;

public class ReservationTimeDoesNotExistsException extends BusinessException {

    private static final String ERROR_MESSAGE = "존재하지 않는 예약 시간입니다.";

    public ReservationTimeDoesNotExistsException() {
        super(ERROR_MESSAGE);
    }

    @Override
    public HttpStatusCode getStatusCode() {
        return HttpStatus.NOT_FOUND;
    }

    @Override
    public ProblemDetail getBody() {
        return ProblemDetail.forStatusAndDetail(getStatusCode(), "존재하지 않는 예약 시간입니다.");
    }
}
