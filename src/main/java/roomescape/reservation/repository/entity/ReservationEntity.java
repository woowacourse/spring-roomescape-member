package roomescape.reservation.repository.entity;

import java.time.LocalDate;

public record ReservationEntity(
        Long id,
        String name,
        LocalDate date,
        Long timeId,
        Long themeId
) {

    public ReservationEntity(final String name, final LocalDate date, final Long timeId, final Long themeId) {
        this(null, name, date, timeId, themeId);
    }
}
