package roomescape.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;

public class ThemeNotFoundException extends BusinessException {

    private static final String CLIENT_TITLE = "조회 실패";
    private static final String CLIENT_DETAIL = "해당하는 테마 정보를 찾을 수 없어 요청을 처리하지 못했습니다. 테마 이름을 다시 확인해 주세요.";

    public ThemeNotFoundException(String logMessage) {
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
