package roomescape.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;

public class ThemeInUseException extends BusinessException {

    private static final String CLIENT_TITLE = "삭제 불가";
    private static final String CLIENT_DETAIL = "해당 테마로 이미 잡혀있는 예약이 존재하여 테마 정보를 삭제할 수 없습니다. 관련된 예약을 먼저 정리해 주세요.";

    public ThemeInUseException(String logMessage) {
        super(logMessage);
    }

    @Override
    public HttpStatusCode getStatusCode() {
        return HttpStatus.CONFLICT;
    }

    @Override
    public ProblemDetail getBody() {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(getStatusCode(), CLIENT_DETAIL);
        problemDetail.setTitle(CLIENT_TITLE);
        return problemDetail;
    }
}
