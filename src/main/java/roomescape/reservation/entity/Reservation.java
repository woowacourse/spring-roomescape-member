package roomescape.reservation.entity;

import java.time.LocalDateTime;

public class Reservation {

    private final long id;
    private final String name;
    private final LocalDateTime dateTime;

    public Reservation(long id, String name, LocalDateTime dateTime) {
        this.id = id;
        this.name = name;
        this.dateTime = dateTime;
    }

    public boolean sameId(long id) {
        return this.id == id;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }
}
