package roomescape.dto;

import java.time.LocalDate;

public record ReservationResponse(
    Long id,
    String name,
    LocalDate date,
    ReservationTimeResponse time,
    ThemeResponse theme) {
}
