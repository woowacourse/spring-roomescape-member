package roomescape.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;

public class Reservation {

    private final Long id;
    private final String name;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private final LocalDate date;
    private final ReservationTime time;
    private final Long time_id;

    public Reservation(Long id, String name, LocalDate date, ReservationTime time, Long time_id) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.time_id = time_id;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ReservationTime getTime() {
        return time;
    }

    public LocalDate getDate() {
        return date;
    }

    public Long getTime_id() {
        return time_id;
    }
}
