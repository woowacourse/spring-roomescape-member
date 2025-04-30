package roomescape.reservation.repository.entity;

import java.time.LocalDate;

public record ReservationEntity(
        Long id,
        String name,
        LocalDate date,
        Long timeId
) {

    public ReservationEntity(final String name, final LocalDate date, final Long timeId) {
        this(null, name, date, timeId);
    }
}
