package roomescape.reservation.dto;

import java.time.LocalDate;

public class ReservationRequest {
    private LocalDate date;
    private String name;
    private Long time_id;

    public ReservationRequest(LocalDate date, String name, Long time_id) {
        this.date = date;
        this.name = name;
        this.time_id = time_id;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getName() {
        return name;
    }

    public Long getTimeId() {
        return time_id;
    }
}
