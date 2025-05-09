package roomescape.reservation.infrastructure.db.dao;

import java.util.List;
import java.util.Optional;
import roomescape.reservation.domain.entity.ReservationTheme;

public interface ReservationThemeDao {

    List<ReservationTheme> selectAll();

    ReservationTheme insertAndGet(ReservationTheme reservationTheme);

    Optional<ReservationTheme> selectById(Long id);

    void deleteById(Long id);

    List<ReservationTheme> getOrderByThemeBookedCountWithLimit(int limit);
}
