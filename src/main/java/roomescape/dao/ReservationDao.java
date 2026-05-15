package roomescape.dao;


import java.time.LocalDate;
<<<<<<< cycle2
import java.util.List;
import roomescape.domain.Reservation;

public interface ReservationDao extends CommonDao<Reservation> {
    List<Reservation> findAll(int limit, int offset);

    long count();

=======
import roomescape.domain.Reservation;

public interface ReservationDao extends CommonDao<Reservation> {
>>>>>>> bee9827
    boolean existsByThemeIdAndTimeIdAndDate(Long themeId, Long timeId, LocalDate date);

    boolean existsByThemeId(Long themeId);

    boolean existsByTimeId(Long timeId);
}
