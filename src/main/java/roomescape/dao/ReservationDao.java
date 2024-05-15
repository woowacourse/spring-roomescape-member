package roomescape.dao;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.stereotype.Repository;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservation.ReservationDate;
import roomescape.domain.reservationtime.ReservationTime;
import roomescape.domain.theme.Theme;

@Repository
public interface ReservationDao {

    List<Reservation> readAll();

    List<Reservation> readFilteredReservation(Long themeId, Long memberId, LocalDate dateFrom, LocalDate dateTo);

    List<Long> readTimeIdsByDateAndThemeId(ReservationDate reservationDate, Long themeId);

    List<Long> readPopularThemeIds(LocalDate startDate, LocalDate endDate);

    Reservation create(Reservation reservation);

    boolean exist(long id);

    boolean exist(ReservationDate reservationDate, ReservationTime reservationTime, Theme theme);

    void delete(long id);

    boolean existByTimeId(Long timeId);

    boolean existByThemeId(Long id);

}
