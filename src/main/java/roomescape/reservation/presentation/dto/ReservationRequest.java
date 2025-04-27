package roomescape.reservation.presentation.dto;

import java.time.LocalDate;

public class ReservationRequest {
    private final LocalDate date;
    private final String name;
    private final Long timeId;

    public ReservationRequest(final LocalDate date, final String name, final Long timeId) {
        this.date = date;
        this.name = name;
        this.timeId = timeId;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getName() {
        return name;
    }

    public Long getTimeId() {
        return timeId;
    }
}
