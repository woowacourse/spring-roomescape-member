package roomescape.reservation.dto;

import java.time.LocalDate;

public class ReservationRequest {
    private LocalDate date;
    private String name;
    private Long timeId;

    public ReservationRequest(LocalDate date, String name, Long timeId) {
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
