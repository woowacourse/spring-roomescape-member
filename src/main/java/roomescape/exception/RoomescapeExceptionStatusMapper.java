package roomescape.exception;

import java.util.Map;
import org.springframework.http.HttpStatus;

public class RoomescapeExceptionStatusMapper {

    private static final HttpStatus DEFAULT_STATUS = HttpStatus.INTERNAL_SERVER_ERROR;

    private static final Map<Class<? extends RoomescapeBaseException>, HttpStatus> STATUS_BY_EXCEPTION = Map.of(
            InvalidDomainException.class, HttpStatus.INTERNAL_SERVER_ERROR,
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
            return DEFAULT_STATUS;
        }
        return status;
    }
}
