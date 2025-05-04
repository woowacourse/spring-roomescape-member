package roomescape.reservation.exception;

import roomescape.common.exception.BusinessException;
import roomescape.reservation.domain.ReservationDate;
import roomescape.theme.domain.ThemeId;
import roomescape.time.domain.ReservationTimeId;

public class DuplicateReservationException extends BusinessException {

    public DuplicateReservationException(final ReservationDate date,
                                         final ReservationTimeId timeId,
                                         final ThemeId themeId) {

        super(buildLoggingMessage(date, timeId, themeId), buildUserMessage());
    }

    private static String buildLoggingMessage(final ReservationDate date,
                                              final ReservationTimeId timeId,
                                              final ThemeId themeId) {
        return "Reservation already exists for date=" + date + ", timeId=" + timeId + ", themeId=" + themeId;
    }

    private static String buildUserMessage() {
        return "선택된 테마와 시간에는 이미 예약이 존재합니다.";
    }
}
