package roomescape.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import roomescape.service.dto.ReservationCreateCommand;

public class ReservationRequest {
    private String name;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    private Long timeId;
    private Long themeId;

    public ReservationRequest() {
    }

    public ReservationCreateCommand toCommand() {
        return new ReservationCreateCommand(name, date, timeId, themeId);
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

    public Long getThemeId() {
        return themeId;
    }
}
