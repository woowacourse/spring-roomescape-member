package roomescape.reservation.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import roomescape.reservation.exception.ReservationErrorCode;
import roomescape.reservation.exception.ReservationPastDateException;
import roomescape.reservation.exception.ReservationPermissionDeniedException;
import roomescape.reservation.exception.ReservationValidationException;

@Getter
@EqualsAndHashCode(of = "id")
public class Reservation {

    private static final int NAME_MAX_LENGTH = 10;

    private final Long id;
    private final String name;
    private final LocalDate date;
    private final Long timeId;

    private Reservation(final Long id, final String name, final LocalDate date, final Long timeId) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.timeId = timeId;
    }

    public static Reservation createNew(final String name, final LocalDate date, final Long timeId) {
        validate(name, date, timeId);
        return new Reservation(null, name, date, timeId);
    }

    public static Reservation of(final long id, final String name, final LocalDate date, final Long timeId) {
        validate(name, date, timeId);
        return new Reservation(id, name, date, timeId);
    }

    private static void validate(final String name, final LocalDate date, final Long timeId) {
        List<String> errors = new ArrayList<>();

        validateName(name, errors);
        validateDate(date, errors);
        validateTimeId(timeId, errors);

        if (!errors.isEmpty()) {
            throw new ReservationValidationException(errors);
        }
    }

    private static void validateName(final String name, final List<String> errors) {
        if (name == null || name.isBlank()) {
            errors.add(ReservationErrorCode.RESERVATION_NAME_NOT_BLANK.getMessage());
            return;
        }

        if (name.length() > NAME_MAX_LENGTH) {
            errors.add(ReservationErrorCode.RESERVATION_NAME_TOO_LONG.getMessage());
        }
    }

    private static void validateDate(final LocalDate date, final List<String> errors) {
        if (date == null) {
            errors.add(ReservationErrorCode.RESERVATION_DATE_NOT_NULL.getMessage());
        }
    }

    private static void validateTimeId(final Long timeId, final List<String> errors) {
        if (timeId == null) {
            errors.add(ReservationErrorCode.RESERVATION_TIME_NOT_NULL.getMessage());
        }
    }

    public Reservation withId(final long id) {
        return new Reservation(id, this.name, this.date, this.timeId);
    }

    public Reservation modify(final LocalDate newDate, final Long newTimeId) {
        validate(name, newDate, newTimeId);
        return new Reservation(id, name, newDate, newTimeId);
    }

    public void validateNotPast(LocalTime time, LocalDateTime now) {
        LocalDateTime reservationDateTime = LocalDateTime.of(date, time);

        if (reservationDateTime.isBefore(now)) {
            throw new ReservationPastDateException(
                    ReservationErrorCode.RESERVATION_PAST_DATE.getMessage()
            );
        }
    }

    public void validateOwner(String requesterName) {
        if (!this.name.equals(requesterName)) {
            throw new ReservationPermissionDeniedException(
                    ReservationErrorCode.RESERVATION_NOT_OWNER.getMessage()
            );
        }
    }

}
