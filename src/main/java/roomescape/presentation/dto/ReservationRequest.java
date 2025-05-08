package roomescape.presentation.dto;

import java.time.LocalDate;
import roomescape.business.domain.PlayTime;
import roomescape.business.domain.Reservation;
import roomescape.business.domain.Theme;

public record ReservationRequest(
        String name, LocalDate date, Long timeId, Long themeId
) {

    public Reservation toDomain(final PlayTime playTime, final Theme theme) {
        return new Reservation(name, date, playTime, theme);
    }
}
