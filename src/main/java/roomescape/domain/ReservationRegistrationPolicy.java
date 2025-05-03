package roomescape.domain;

import static roomescape.exception.ReservationErrorCode.ALREADY_RESERVED;
import static roomescape.exception.ReservationErrorCode.PAST_RESERVATION;

import org.springframework.stereotype.Component;
import roomescape.domain.exception.ImpossibleReservationException;

@Component
public class ReservationRegistrationPolicy {

    public void validate(Reservation candidate, boolean existsDuplicatedReservation) {
        validateNotPast(candidate);
        validateNotDuplicated(existsDuplicatedReservation);
    }

    private void validateNotDuplicated(boolean existingReservations) {
        if (existingReservations) {
            throw new ImpossibleReservationException(ALREADY_RESERVED.getMessage());
        }
    }

    private void validateNotPast(Reservation candidate) {
        if (candidate.isPast()) {
            throw new ImpossibleReservationException(PAST_RESERVATION.getMessage());
        }
    }
}
