package roomescape.reservation.exception;

import roomescape.global.exception.exception.PermissionDeniedException;

public class ReservationPermissionDeniedException extends PermissionDeniedException {
    public ReservationPermissionDeniedException(String message) {
        super(message);
    }
}
