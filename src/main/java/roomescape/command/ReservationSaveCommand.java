package roomescape.command;

import java.time.LocalDate;

public record ReservationSaveCommand(String name, LocalDate date, Long timeId) {

}
