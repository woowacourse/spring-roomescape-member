package roomescape.exception;

import java.time.LocalDate;
import java.time.LocalTime;

public class DuplicatedReservationException extends IllegalArgumentException {

    private static final String ERROR_MESSAGE_FORMAT = "중복된 예약입니다. 다른 때를 선택해 주세요. 현재 선택: 날짜=%s, 시간=%s, 테마 이름=%s";

    public DuplicatedReservationException(LocalDate date, LocalTime time, String themeName) {
        super(ERROR_MESSAGE_FORMAT.formatted(date, time, themeName));
    }
}
