package roomescape.domain;

import java.time.LocalDate;

public record ReservationDate(LocalDate date) {

    public boolean isBefore(LocalDate limitDate) {
        return date.isBefore(limitDate);
    }

    public boolean isLimitDate(LocalDate limitDate) {
        return date.isEqual(limitDate);
    }
}
