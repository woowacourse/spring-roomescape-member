package roomescape.domain.vo;

import java.time.LocalDate;

public record ReservationDate(
        LocalDate value
) {
    public static ReservationDate from(LocalDate date) {
        return new ReservationDate(date);
    }
}
