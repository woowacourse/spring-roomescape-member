package roomescape.presentation.dto.reservation;

import java.time.LocalDate;
import roomescape.business.domain.PlayTime;
import roomescape.business.domain.Reservation;
import roomescape.business.domain.Theme;
import roomescape.business.domain.User;

public record ReservationRequest(
        LocalDate date, Long timeId, Long themeId
) {

    public Reservation toDomain(
            final User user,
            final PlayTime playTime,
            final Theme theme
    ) {
        return new Reservation(user, date, playTime, theme);
    }
}
