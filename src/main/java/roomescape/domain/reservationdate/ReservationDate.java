package roomescape.domain.reservationdate;

import java.time.LocalDate;

public class ReservationDate {

    private final Long id;
    private final LocalDate date;

    public ReservationDate(Long id, LocalDate date) {
        this.id = id;
        this.date = date;
    }
}
