package roomescape.exception;

import java.time.LocalDate;
import java.time.LocalTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;

public class DuplicatedReservationException extends BusinessException {

    private static final String LOG_MESSAGE_FORMAT = "중복된 예약 발생: 날짜=%s, 시간=%s, 테마 이름=%s";
    private static final String CLIENT_TITLE = "이미 예약된 시간입니다.";
    private static final String CLIENT_DETAIL = "선택하신 날짜와 시간은 이미 예약이 완료되어 예약을 진행할 수 없습니다. 다른 일정을 선택해 주세요.";

    public DuplicatedReservationException(LocalDate date, LocalTime time, String themeName) {
        super(LOG_MESSAGE_FORMAT.formatted(date, time, themeName));
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
