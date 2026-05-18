package roomescape.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;

public class PastDateModificationException extends BusinessException {

    private static final String CLIENT_TITLE = "수정 불가";
    private static final String CLIENT_DETAIL = "이미 이용 시간이 지난 예약이어서 정보를 변경할 수 없습니다. 변경 가능한 예약을 다시 확인해 주세요.";

    public PastDateModificationException(String logMessage) {
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
