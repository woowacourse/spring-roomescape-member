package roomescape.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import roomescape.service.dto.ReservationCreateCommand;

public record ReservationRequest(
        String name,
        @JsonFormat(pattern = "yyyy-MM-dd") LocalDate date,
        Long timeId,
        Long themeId
) {
    public ReservationCreateCommand toCommand() {
        return new ReservationCreateCommand(name, date, timeId, themeId);
    }
}
