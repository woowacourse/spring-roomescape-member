package roomescape.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;

public class CannotDeleteReservationTimeException extends BusinessException {

    private static final String DEFAULT_ERROR_MESSAGE = "예약 시간을 삭제할 수 없습니다. ";

    public CannotDeleteReservationTimeException(String additionalMessage) {
        super(DEFAULT_ERROR_MESSAGE + additionalMessage);
    }

    @Override
    public HttpStatusCode getStatusCode() {
        return HttpStatus.CONFLICT;
    }

    @Override
    public ProblemDetail getBody() {
        return ProblemDetail.forStatusAndDetail(getStatusCode(), "예약 시간을 삭제할 수 없습니다.");
    }
}
