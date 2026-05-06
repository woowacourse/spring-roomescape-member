package roomescape.date.dto.request;

import java.time.LocalDate;

public record ReservationDateSaveDto(
        LocalDate date
) {
}
