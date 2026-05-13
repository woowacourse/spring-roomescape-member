package roomescape.exception;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ReservationByPastDateTimeException extends IllegalArgumentException {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final String ERROR_MESSAGE_FORMAT = "과거 시점에 대한 예약은 불가합니다. 다른 날짜나 시간을 선택해 주세요. 현재=%s, 예약 요청 시점=%s";

    public ReservationByPastDateTimeException(LocalDateTime now, LocalDateTime targetDateTime) {
        super(ERROR_MESSAGE_FORMAT.formatted(now.format(FORMATTER), targetDateTime.format(FORMATTER)));
    }
}
