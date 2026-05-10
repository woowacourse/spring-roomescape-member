package roomescape.domain.reservationdate;

import java.time.LocalDate;
import lombok.Getter;

@Getter
public class ReservationDate {

    private final Long id;
    private final LocalDate date;

    public ReservationDate(Long id, LocalDate date) {
        this.id = id;
        this.date = date;
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
