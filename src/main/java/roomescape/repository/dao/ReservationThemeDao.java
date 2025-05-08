package roomescape.repository.dao;

import java.util.List;
import java.util.Optional;

import roomescape.domain.ReservationTheme;

public interface ReservationThemeDao {

    List<ReservationTheme> selectAll();

    ReservationTheme insertAndGet(ReservationTheme reservationTheme);

    Optional<ReservationTheme> selectById(Long id);

    void deleteById(Long id);

    List<ReservationTheme> orderByThemeBookedCountWithLimit(int limit);
}
