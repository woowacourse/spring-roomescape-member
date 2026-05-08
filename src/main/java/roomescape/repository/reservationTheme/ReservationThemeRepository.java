package roomescape.repository.reservationTheme;

import java.util.List;
import java.util.Optional;
import roomescape.domain.reservationTheme.PopularThemeCondition;
import roomescape.domain.reservationTheme.ReservationTheme;
import roomescape.domain.reservationTheme.ReservationThemeCommand;
import roomescape.domain.reservationTheme.ReservationThemeWithCount;

public interface ReservationThemeRepository {
    ReservationTheme addTheme(ReservationThemeCommand reservationThemeCommand);
    List<ReservationTheme> getAllTheme();
    Optional<ReservationTheme> getTheme(long id);
    void deleteTheme(long id);
    List<ReservationThemeWithCount> getPopularTheme(PopularThemeCondition popularThemeCondition);
}
