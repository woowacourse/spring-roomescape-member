package roomescape.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import roomescape.service.dto.ReservationUpdateCommand;

public class ReservationUpdateRequest {
    private String name;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    private Long timeId;

    public ReservationUpdateRequest() {
    }

    public ReservationUpdateCommand toCommand(Long id) {
        return new ReservationUpdateCommand(id, name, date, timeId);
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
