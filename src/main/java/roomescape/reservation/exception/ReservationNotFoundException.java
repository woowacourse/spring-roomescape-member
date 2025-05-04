package roomescape.reservation.exception;

import roomescape.common.exception.BusinessException;
import roomescape.reservation.domain.ReservationId;

public class ReservationNotFoundException extends BusinessException {

    public ReservationNotFoundException(final ReservationId id) {
        super(buildLoggingMessage(id), buildUserMessage());
    }

    private static String buildLoggingMessage(final ReservationId id) {
        return "Tried to delete reservation that does not exist. id=" + id;
    }

    private static String buildUserMessage() {
        return "삭제할 예약이 존재하지 않습니다.";
    }
}
