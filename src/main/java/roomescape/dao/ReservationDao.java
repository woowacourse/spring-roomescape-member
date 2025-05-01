package roomescape.dao;

import java.time.LocalDate;
import java.util.List;
import roomescape.domain_entity.Id;
import roomescape.domain_entity.Reservation;
import roomescape.domain_entity.ReservationTime;

public interface ReservationDao {

    List<Reservation> findAll();

    long create(Reservation reservation);

    void deleteById(Id id);

    Boolean existByTimeId(Id timeId);

    Boolean existBySameDateTime(Reservation reservation);

    Boolean existByDateTimeAndTheme(LocalDate date, ReservationTime time, Long themeId);
}
