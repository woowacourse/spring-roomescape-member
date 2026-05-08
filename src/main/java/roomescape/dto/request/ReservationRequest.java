package roomescape.dto.request;

import roomescape.service.command.ReservationSaveCommand;

import java.time.LocalDate;

public record ReservationRequest(String name, LocalDate date, Long timeId, Long themeId) {

    public ReservationSaveCommand toSaveCommand() {
        return new ReservationSaveCommand(name, date, timeId, themeId);
    }
}
