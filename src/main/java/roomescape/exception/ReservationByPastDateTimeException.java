package roomescape.exception;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;

public class ReservationByPastDateTimeException extends BusinessException {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final String ERROR_MESSAGE_FORMAT = "과거 시점에 대한 예약은 불가합니다. 다른 날짜나 시간을 선택해 주세요. 현재=%s, 예약 요청 시점=%s";

    public ReservationByPastDateTimeException(LocalDateTime now, LocalDateTime targetDateTime) {
        super(ERROR_MESSAGE_FORMAT.formatted(now.format(FORMATTER), targetDateTime.format(FORMATTER)));
    }

    @Override
    public HttpStatusCode getStatusCode() {
        return HttpStatus.UNPROCESSABLE_ENTITY;
    }

    @Override
    public ProblemDetail getBody() {
        return ProblemDetail.forStatusAndDetail(getStatusCode(), "과거 시점의 예약입니다.");
    }
}
