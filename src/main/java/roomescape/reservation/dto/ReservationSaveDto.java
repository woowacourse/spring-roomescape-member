package roomescape.reservation.dto;

import java.time.LocalDate;

public record ReservationSaveDto(
        String name,
        LocalDate date, // todo dateId
        Long timeId,
        Long themeId
) {
}
