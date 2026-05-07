package roomescape.domain;

import roomescape.exception.DomainException;
import roomescape.exception.ErrorCode;

import java.time.LocalDate;

public record ReservationDate(LocalDate value) {

    public ReservationDate {
        if (value == null) {
            throw new DomainException(ErrorCode.INVALID_RESERVATION_DATE);
        }
    }
}
