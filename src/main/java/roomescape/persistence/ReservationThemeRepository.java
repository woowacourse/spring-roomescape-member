package roomescape.persistence;

import java.util.List;
import roomescape.business.ReservationTheme;

public interface ReservationThemeRepository {

    List<ReservationTheme> findAll();
}
