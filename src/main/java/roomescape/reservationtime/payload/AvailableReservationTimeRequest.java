package roomescape.reservationtime.payload;

import java.time.LocalDate;

public record AvailableReservationTimeRequest(LocalDate date, Long themeId) {

    public boolean isEmpty() {
        return date == null && themeId == null;
    }

    public boolean isComplete() {
        return date != null && themeId != null;
    }
}
