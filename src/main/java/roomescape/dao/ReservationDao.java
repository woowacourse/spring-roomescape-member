package roomescape.dao;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Repository;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservation.ReservationDate;

@Repository
public interface ReservationDao {

    List<Reservation> readAll();

    List<Long> readTimeIdsByDateAndThemeId(ReservationDate reservationDate, Long themeId);

    List<Long> readPopularThemeIds(LocalDate startDate, LocalDate endDate);

    Reservation create(Reservation reservation);

    boolean exist(long id);

    boolean exist(Reservation reservation);

    void delete(long id);

    boolean existByTimeId(Long timeId);

    boolean existByThemeId(Long id);
}
