package roomescape.domain.reservationdate;

import java.time.LocalDate;
import lombok.Getter;

@Getter
public class ReservationDate {

    private final Long id;
    private final LocalDate playDay;

    public ReservationDate(Long id, LocalDate playDay) {
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
}
