package roomescape.service.command;

import java.time.LocalDate;

public record ReservationSaveCommand(String name, LocalDate date, Long timeId, Long themeId) {

}
