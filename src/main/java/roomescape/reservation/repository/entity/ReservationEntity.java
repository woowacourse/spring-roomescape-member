package roomescape.reservation.repository.entity;

import java.time.LocalDate;

public class ReservationEntity {

    private final Long id;
    private final String name;
    private final LocalDate date;
    private final Long timeId;

    public ReservationEntity(Long id, String name, LocalDate date, Long timeId) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.timeId = timeId;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDate getDate() {
        return date;
    }

    public Long getTimeId() {
        return timeId;
    }
}
