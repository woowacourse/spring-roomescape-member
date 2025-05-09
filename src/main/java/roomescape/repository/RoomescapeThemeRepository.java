package roomescape.repository;

import java.util.List;
import java.util.Optional;
import roomescape.domain.ReservationTheme;

public interface RoomescapeThemeRepository {

    Optional<ReservationTheme> findById(final Long id);

    List<ReservationTheme> findTopThemeOrderByCountWithinDaysDesc(int days);

    List<ReservationTheme> findAll();

    ReservationTheme save(final ReservationTheme reservationTheme);

    boolean deleteById(final Long id);
}
