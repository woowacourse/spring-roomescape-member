package roomescape.dao;

import java.util.List;
import org.springframework.stereotype.Repository;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservation.ReservationDate;
import roomescape.domain.reservationtime.ReservationTime;
import roomescape.domain.theme.Theme;

@Repository
public interface ReservationDao {

    List<Reservation> readAll();

    Reservation create(Reservation reservation);

    Boolean exist(long id);

    Boolean exist(ReservationDate reservationDate, ReservationTime reservationTime, Theme theme);

    void delete(long id);

    boolean existByTimeId(Long timeId);

    boolean existByThemeId(Long id);
}
