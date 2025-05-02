package roomescape.domain;

import static roomescape.exception.ReservationErrorCode.ALREADY_RESERVED;
import static roomescape.exception.ReservationErrorCode.PAST_RESERVATION;

import java.util.List;
import org.springframework.stereotype.Component;
import roomescape.domain.exception.ImpossibleReservationException;

@Component
public class ReservationRegistrationPolicy {

    public void validate(Reservation candidate, List<Reservation> existingReservations) {
        validateNotPast(candidate);
        validateNotDuplicated(candidate, existingReservations);
    }

    private void validateNotDuplicated(Reservation candidate, List<Reservation> existingReservations) {
        boolean isDuplicated = existingReservations.stream()
                .anyMatch(r -> r.isDuplicatedWith(candidate));
        if (isDuplicated) {
            throw new ImpossibleReservationException(ALREADY_RESERVED.getMessage());
        }
    }

    private void validateNotPast(Reservation candidate) {
        if (candidate.isPast()) {
            throw new ImpossibleReservationException(PAST_RESERVATION.getMessage());
        }
    }
}
