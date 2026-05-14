package roomescape.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;

public class CannotDeleteReservationException extends BusinessException {

    private static final String ERROR_MESSAGE = "예약을 삭제할 수 없습니다. ";

    public CannotDeleteReservationException(String additionalMessage) {
        super(ERROR_MESSAGE + additionalMessage);
    }

    public CannotDeleteReservationException(String additionalMessage, Throwable cause) {
        super(additionalMessage, cause);
    }

    @Override
    public HttpStatusCode getStatusCode() {
        return HttpStatus.UNPROCESSABLE_ENTITY;
    }

    @Override
    public ProblemDetail getBody() {
        final String clientMessage = "과거 시점의 예약이어서 삭제할 수 없습니다. 다른 예약을 선택해 주세요.";
        return ProblemDetail.forStatusAndDetail(getStatusCode(), clientMessage);
    }
}
