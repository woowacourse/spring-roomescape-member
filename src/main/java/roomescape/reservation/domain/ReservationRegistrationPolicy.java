package roomescape.reservation.domain;

import org.springframework.stereotype.Component;
import roomescape.reservation.domain.exception.ImpossibleReservationException;
import roomescape.exception.ReservationErrorCode;

@Component
public class ReservationRegistrationPolicy {

    public void validate(Reservation candidate, boolean existsDuplicatedReservation) {
        validateNotPast(candidate);
        validateNotDuplicated(existsDuplicatedReservation);
    }

    private void validateNotDuplicated(boolean existingReservations) {
        if (existingReservations) {
            throw new ImpossibleReservationException(ReservationErrorCode.ALREADY_RESERVED.getMessage());
        }
    }

    private void validateNotPast(Reservation candidate) {
        if (candidate.isPast()) {
            throw new ImpossibleReservationException(ReservationErrorCode.PAST_RESERVATION.getMessage());
        }
    }
}
