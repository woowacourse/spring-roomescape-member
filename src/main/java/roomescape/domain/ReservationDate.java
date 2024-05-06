package roomescape.domain;

import java.time.LocalDate;

public record ReservationDate(LocalDate date) {

    public boolean isPastDate() {
        return date.isBefore(LocalDate.now());
    }

    public boolean isPresentDate() {
        return date.isEqual(LocalDate.now());
    }
}
