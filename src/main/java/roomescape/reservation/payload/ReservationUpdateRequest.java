package roomescape.reservation.payload;

import java.time.LocalDate;

public record ReservationUpdateRequest(
        LocalDate date,
        Long timeId,
        Long themeId
) {

    public boolean isEmpty() {
        return date == null && timeId == null && themeId == null;
    }
}
