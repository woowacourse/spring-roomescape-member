package roomescape.domain.reservationdate;

import java.time.LocalDate;
import lombok.Getter;
import roomescape.support.exception.ReservationDateErrorCode;
import roomescape.support.exception.RoomescapeException;

@Getter
public class ReservationDate {

    private final Long id;
    private final LocalDate playDay;

    public ReservationDate(Long id, LocalDate playDay) {
        validate(playDay);
        this.id = id;
        this.playDay = playDay;
    }

    public static ReservationDate of(long dateId, LocalDate date) {
        return new ReservationDate(dateId, date);
    }

    public static ReservationDate createWithoutId(LocalDate reservationDate) {
        return new ReservationDate(
            null,
            reservationDate
        );
    }

    private static void validate(LocalDate reservationDate) {
        if (reservationDate == null) {
            throw new RoomescapeException(ReservationDateErrorCode.INVALID_RESERVATION_DATE);
        }
    }
}
