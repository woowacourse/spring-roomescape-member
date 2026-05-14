package roomescape.dto;

import java.time.LocalDate;

public record ReservationUpdateDtoDateAndTimeIdOnly(
        LocalDate date,
        Long timeId
) {
}
