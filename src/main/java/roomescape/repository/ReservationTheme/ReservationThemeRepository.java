package roomescape.repository.ReservationTheme;

import java.util.List;
import java.util.Optional;
import roomescape.domain.ReservationTheme.ReservationTheme;
import roomescape.domain.ReservationTheme.ReservationThemeCommand;

public interface ReservationThemeRepository {
    ReservationTheme addTheme(ReservationThemeCommand reservationThemeCommand);
    List<ReservationTheme> getAllTheme();
    Optional<ReservationTheme> getTheme(long id);
    void deleteTheme(long id);
}
