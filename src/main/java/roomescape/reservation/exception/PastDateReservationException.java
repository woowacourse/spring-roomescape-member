package roomescape.reservation.exception;

import roomescape.common.exception.BusinessException;
import roomescape.reservation.domain.ReservationDate;

import java.time.LocalDateTime;

public class PastDateReservationException extends BusinessException {

    public PastDateReservationException(final ReservationDate date, final LocalDateTime now) {
        super(buildLoggingMessage(date, now), buildUserMessage());
    }

    private static String buildLoggingMessage(final ReservationDate date, final LocalDateTime now) {
        return "Attempted to reserve with past date. input=" + date + ", now=" + now;
    }

    private static String buildUserMessage() {
        return "지난 날짜는 예약할 수 없습니다.";
    }
}
