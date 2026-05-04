package roomescape.domain.reservation;

import lombok.Getter;
import roomescape.domain.reservationdate.ReservationDate;
import roomescape.domain.reservationtime.ReservationTime;
import roomescape.support.exception.ReservationErrorCode;
import roomescape.support.exception.ReservationTimeErrorCode;
import roomescape.support.exception.RoomescapeException;

@Getter
public class Reservation {

    private final Long id;
    private final String name;
    private final ReservationDate date;
    private final ReservationTime time;

    private Reservation(Long id, String name, ReservationDate date, ReservationTime time) {
        validate(name, date, time);
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
    }

    public static Reservation createWithoutId(String name, ReservationDate date, ReservationTime time) {
        return new Reservation(
            null,
            name,
            date,
            time
        );
    }

    public static Reservation of(
        long id,
        String name,
        ReservationDate date,
        ReservationTime time
    ) {
        return new Reservation(id, name, date, time);
    }

    private static void validate(String name, ReservationDate date, ReservationTime time) {
        if (name == null || name.isBlank()) {
            throw new RoomescapeException(ReservationErrorCode.INVALID_RESERVATION_NAME);
        }
        if (date == null) {
            throw new RoomescapeException(ReservationErrorCode.INVALID_RESERVATION_DATE);
        }
        if (time == null) {
            throw new RoomescapeException(ReservationTimeErrorCode.INVALID_RESERVATION_TIME);
        }
    }
}
