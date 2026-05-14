package roomescape.domain.reservation.validator;

import java.time.Clock;
import java.time.LocalDate;
import roomescape.domain.global.exception.custom.ForbiddenException;
import roomescape.domain.global.exception.custom.UnprocessableEntityException;
import roomescape.domain.global.exception.error.ErrorCode;
import roomescape.domain.reservation.entity.Reservation;
import roomescape.domain.time.entity.Time;

public final class ReservationValidator {

    private ReservationValidator() {}

    public static void validateOwner(String name, Reservation reservation) {
        if (!reservation.isOwner(name)) {
            throw new ForbiddenException(ErrorCode.RESERVATION_FORBIDDEN);
        }
    }

    public static void validateDateAccessable(Reservation reservation, Clock clock) {
        if (reservation.isPast(clock)) {
            throw new UnprocessableEntityException(ErrorCode.RESERVATION_ALREADY_PASSED);
        }
    }

    public static void validateDateTimeChangeable(LocalDate date, Time time, Clock clock) {
        LocalDate nowDate = LocalDate.now(clock);
        if (date.isBefore(LocalDate.now(clock)) || (date.isEqual(nowDate) && time.isPast(clock))) {
            throw new UnprocessableEntityException(ErrorCode.RESERVATION_TIME_ALREADY_PASSED);
        }
    }


}
