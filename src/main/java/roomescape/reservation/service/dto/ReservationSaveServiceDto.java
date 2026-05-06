package roomescape.reservation.service.dto;

import java.time.LocalDate;

public class ReservationSaveServiceDto {
    private final String name;
    private final LocalDate date;
    private final Long timeId;

    public ReservationSaveServiceDto(String name, LocalDate date, Long timeId) {
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
