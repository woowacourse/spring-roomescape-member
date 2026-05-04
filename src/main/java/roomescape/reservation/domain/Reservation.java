package roomescape.reservation.domain;

import java.time.LocalDate;
import lombok.Getter;

@Getter
public class Reservation {
    private final Long id;
    private String name;
    private LocalDate date;
    private ReservationTime time;
    private Theme theme;

    public Reservation(Long id,
                       String name,
                       LocalDate date,
                       ReservationTime time,
                       Theme theme) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }
}
