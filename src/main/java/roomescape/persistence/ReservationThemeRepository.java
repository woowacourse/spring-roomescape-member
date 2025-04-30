package roomescape.persistence;

import java.util.List;
import roomescape.business.ReservationTheme;

public interface ReservationThemeRepository {

    List<ReservationTheme> findAll();

    Long add(ReservationTheme reservationTheme);

    boolean existByName(String name);
}
