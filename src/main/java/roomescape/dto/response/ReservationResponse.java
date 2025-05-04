package roomescape.dto.response;

import java.time.LocalDate;
import java.time.LocalTime;

public record ReservationResponse(Long id, String name, LocalTime time, LocalDate date, String themeName) {

}
