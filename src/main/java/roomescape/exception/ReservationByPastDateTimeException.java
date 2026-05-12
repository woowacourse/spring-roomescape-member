package roomescape.exception;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ReservationByPastDateTimeException extends IllegalArgumentException {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_DATE;
    private static final String ERROR_MESSAGE_FORMAT = "과거 시점에 대한 예약은 불가합니다. 현재=%s, 예약 요청 시점=%s";

    public ReservationByPastDateTimeException(LocalDateTime now, LocalDateTime targetDateTime) {
        super(ERROR_MESSAGE_FORMAT.formatted(now.format(FORMATTER), targetDateTime.format(FORMATTER)));
    }
}
