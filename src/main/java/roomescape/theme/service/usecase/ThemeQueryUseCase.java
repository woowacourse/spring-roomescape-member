package roomescape.theme.service.usecase;

import roomescape.reservation.domain.ReservationDate;
import roomescape.theme.domain.Theme;
import roomescape.theme.domain.ThemeId;

import java.util.List;

public interface ThemeQueryUseCase {

    List<Theme> getAll();

    Theme get(ThemeId id);

    List<Theme> getRanking(ReservationDate startDate, ReservationDate endDate, int count);
}
