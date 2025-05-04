package roomescape.reservation.exception;

import roomescape.common.exception.BusinessException;
import roomescape.time.domain.ReservationTime;

import java.time.LocalDateTime;

public class PastTimeReservationException extends BusinessException {

    public PastTimeReservationException(final ReservationTime time, final LocalDateTime now) {
        super(buildLoggingMessage(time, now), buildUserMessage());
    }

    private static String buildLoggingMessage(final ReservationTime time, final LocalDateTime now) {
        return "Attempted to reserve with past time. input=" + time + ", now=" + now;
    }

    private static String buildUserMessage() {
        return "이미 지난 시간에는 예약할 수 없습니다.";
    }
}
