package roomescape.repository;

import java.util.List;
import roomescape.domain.ReservationTheme;

public interface RoomescapeThemeRepository {

    ReservationTheme findById(final Long id);

    List<ReservationTheme> findAll();

    ReservationTheme save(final ReservationTheme reservationTheme);

    int deleteById(final long id);
}
