package roomescape.infrastructure.db.dao;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import roomescape.domain.entity.ReservationTime;

public interface ReservationTimeDao {

    List<ReservationTime> selectAll();

    ReservationTime insertAndGet(ReservationTime reservationTime);

    Optional<ReservationTime> selectById(Long id);

    void deleteById(Long id);

    List<ReservationTime> selectAllByThemeIdAndDate(Long themeId, LocalDate date);
}
