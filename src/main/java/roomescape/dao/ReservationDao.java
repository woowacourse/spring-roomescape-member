package roomescape.dao;

import java.time.LocalDate;
import java.util.List;
import roomescape.domain_entity.Reservation;
import roomescape.domain_entity.ReservationTime;

public interface ReservationDao {

    List<Reservation> findAll();

    Long create(Reservation reservation);

    void deleteById(Long id);

    Boolean existByTimeId(Long timeId);

    Boolean existBySameDateTime(Reservation reservation);

    Boolean existByDateTimeAndTheme(LocalDate date, ReservationTime time, Long themeId);

    List<Long> findRank(LocalDate startDate, LocalDate endDate);
}
