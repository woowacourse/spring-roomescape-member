package roomescape.service.dto;

import java.time.LocalDate;

public class ReservationCreateCommand {
    private final String name;
    private final LocalDate date;
    private final Long timeId;

    public ReservationCreateCommand(String name, LocalDate date, Long timeId) {
        this.name = name;
        this.date = date;
        this.timeId = timeId;
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
