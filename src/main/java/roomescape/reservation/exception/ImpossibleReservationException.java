package roomescape.reservation.exception;

import roomescape.shared.domain.exception.DomainException;

public class ImpossibleReservationException extends DomainException {
    public ImpossibleReservationException(String message) {
        super(message, "예약 불가", "IMPOSSIBLE_RESERVATION");
    }
}
