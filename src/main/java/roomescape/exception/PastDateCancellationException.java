package roomescape.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;

public class PastDateCancellationException extends BusinessException {

    private static final String CLIENT_TITLE = "취소 불가";
    private static final String CLIENT_DETAIL = "이미 이용 시간이 지난 예약이어서 취소할 수 없습니다. 취소 가능한 예약을 다시 확인해 주세요.";

    public PastDateCancellationException(String logMessage) {
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
