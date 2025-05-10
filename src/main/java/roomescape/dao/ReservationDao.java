package roomescape.dao;

import java.time.LocalDate;
import java.util.List;
import roomescape.entity.Reservation;
import roomescape.entity.ReservationTime;

public interface ReservationDao {

    List<Reservation> findAll();

    Reservation create(Reservation reservation);

    void deleteById(Long id);

    List<Long> findMostReservedThemeIdsBetween(LocalDate startDate, LocalDate endDate);

    List<Reservation> findByThemeAndMemberAndDate(Long themeId, Long memberId, LocalDate dateFrom, LocalDate dateTo);

    boolean existByTimeId(Long timeId);

    boolean existByThemeId(Long themeId);

    boolean existBySameDateTime(Reservation reservation);

    boolean existByDateTimeAndTheme(LocalDate date, ReservationTime time, Long themeId);
}
