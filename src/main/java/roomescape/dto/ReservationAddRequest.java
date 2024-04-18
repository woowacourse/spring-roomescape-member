package roomescape.dto;

import java.time.LocalDate;

public class ReservationAddRequest {
    private LocalDate date;
    private String name;
    private Long timeId;

    public ReservationAddRequest() {

    }

    public ReservationAddRequest(LocalDate date, String name, Long timeId) {
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
