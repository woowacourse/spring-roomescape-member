package roomescape.exception;

import java.util.Map;
import org.springframework.http.HttpStatus;

public class RoomescapeExceptionStatusMapper {

    private static final Map<Class<? extends RoomescapeBaseException>, HttpStatus> STATUS_BY_EXCEPTION = Map.of(
            ResourceNotFoundException.class, HttpStatus.NOT_FOUND,
            ReservationOwnerMismatchException.class, HttpStatus.FORBIDDEN,
            DuplicateReservationException.class, HttpStatus.CONFLICT,
            ReservationTimeInUseException.class, HttpStatus.CONFLICT,
            PastDateTimeReservationException.class, HttpStatus.UNPROCESSABLE_ENTITY,
            PastReservationModificationException.class, HttpStatus.UNPROCESSABLE_ENTITY
    );

    public HttpStatus statusOf(RoomescapeBaseException exception) {
        HttpStatus status = STATUS_BY_EXCEPTION.get(exception.getClass());
        if (status == null) {
            throw new IllegalArgumentException(
                    "HTTP status is not mapped for " + exception.getClass().getSimpleName()
            );
        }
        return status;
    }
}
