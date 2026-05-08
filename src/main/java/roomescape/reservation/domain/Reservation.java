package roomescape.reservation.domain;

import java.time.LocalDate;
import java.time.LocalTime;
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

    public Long getTimeId() {
        return this.time.getId();
    }

    public LocalTime getStartAt(){
        return this.time.getStartAt();
    }

    public Long getThemeId() {
        return this.theme.getId();
    }
}
