package roomescape.exception;

import java.time.LocalDate;
import java.time.LocalTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;

public class DuplicatedReservationException extends BusinessException {

    private static final String ERROR_MESSAGE_FORMAT = "중복된 예약입니다. 다른 때를 선택해 주세요. 현재 선택: 날짜=%s, 시간=%s, 테마 이름=%s";

    public DuplicatedReservationException(LocalDate date, LocalTime time, String themeName) {
        super(ERROR_MESSAGE_FORMAT.formatted(date, time, themeName));
    }

    @Override
    public HttpStatusCode getStatusCode() {
        return HttpStatus.CONFLICT;
    }

    @Override
    public ProblemDetail getBody() {
        return ProblemDetail.forStatusAndDetail(getStatusCode(), "중복된 예약입니다.");
    }
}
