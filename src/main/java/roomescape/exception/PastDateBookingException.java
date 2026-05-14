package roomescape.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;

public class PastDateBookingException extends BusinessException {

    private static final String CLIENT_TITLE = "예약 불가";
    private static final String CLIENT_DETAIL = "선택하신 시간이 현재보다 이전이어서 예약할 수 없습니다. 현재 시간 이후의 일정을 선택해 주세요.";

    public PastDateBookingException(String logMessage) {
        super(logMessage);
    }

    @Override
    public HttpStatusCode getStatusCode() {
        return HttpStatus.UNPROCESSABLE_ENTITY;
    }

    @Override
    public ProblemDetail getBody() {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(getStatusCode(), CLIENT_DETAIL);
        problemDetail.setTitle(CLIENT_TITLE);
        return problemDetail;
    }
}
