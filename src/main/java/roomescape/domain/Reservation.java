package roomescape.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.time.LocalDate;
import java.util.Objects;

public class Reservation {
    private final Long id;
    private final PlayerName name;
    private final LocalDate date;
    private final ReservationTime time;

    @JsonCreator
    public Reservation(Long id, PlayerName name, LocalDate date, ReservationTime time) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
    }

    public Reservation(PlayerName name, LocalDate date, ReservationTime time) {
        this(null, name, date, time);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getName();
    }

    public LocalDate getDate() {
        return date;
    }

    public ReservationTime getTime() {
        return time;
    }

    public Reservation withId(long id) {
        return new Reservation(id, name, date, time);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Reservation that)) {
            return false;
        }
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
