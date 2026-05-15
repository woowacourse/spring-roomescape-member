package roomescape.domain.reservation.validator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import roomescape.domain.global.exception.custom.BusinessException;
import roomescape.domain.global.exception.error.ErrorCode;
import roomescape.domain.reservation.entity.Reservation;
import roomescape.domain.time.entity.Time;

public final class ReservationValidator {

    private ReservationValidator() {}

    public static void validateOwner(String name, Reservation reservation) {
        if (!reservation.isOwner(name)) {
            throw new BusinessException(ErrorCode.RESERVATION_FORBIDDEN);
        }
    }

    public static void validateDateAccessable(Reservation reservation, LocalDateTime now) {
        if (reservation.isPast(now)) {
            throw new BusinessException(ErrorCode.RESERVATION_ALREADY_PASSED);
        }
    }

    public static void validateDateTimeChangeable(LocalDate date, Time time, LocalDateTime now) {
        LocalDate nowDate = now.toLocalDate();
        LocalTime nowTime = now.toLocalTime();

        if (date.isBefore(nowDate) || (date.isEqual(nowDate) && time.isPast(nowTime))) {
            throw new BusinessException(ErrorCode.RESERVATION_TIME_ALREADY_PASSED);
        }
    }


}
