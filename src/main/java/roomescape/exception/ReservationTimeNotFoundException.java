package roomescape.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;

public class ReservationTimeNotFoundException extends BusinessException {

    private static final String CLIENT_TITLE = "조회 실패";
    private static final String CLIENT_DETAIL = "해당하는 예약 시간 정보를 찾을 수 없어 요청을 처리하지 못했습니다. 선택하신 시간을 다시 확인해 주세요.";

    public ReservationTimeNotFoundException(String logMessage) {
        super(logMessage);
    }

    @Override
    public HttpStatusCode getStatusCode() {
        return HttpStatus.NOT_FOUND;
    }

    @Override
    public ProblemDetail getBody() {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(getStatusCode(), CLIENT_DETAIL);
        problemDetail.setTitle(CLIENT_TITLE);
        return problemDetail;
    }
}
