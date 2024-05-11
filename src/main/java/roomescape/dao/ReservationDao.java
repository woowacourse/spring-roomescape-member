package roomescape.dao;

import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservation.ReservationTime;

import java.time.LocalDate;
import java.util.List;

public interface ReservationDao {

    Reservation save(final Reservation reservation);

    List<Reservation> findAll();

    List<Reservation> findAllByDateAndTimeAndThemeId(final LocalDate date, final ReservationTime time, final Long themeId);

    List<Reservation> findAllByThemeAndMemberAndPeriod(final Long themeId, final Long memberId, final LocalDate dateFrom, final LocalDate dateTo);

    boolean existById(final Long id);

    void deleteById(final Long id);

    int countByTimeId(final Long timeId);

    List<Long> findAllTimeIdsByDateAndThemeId(final LocalDate date, final Long themeId);
}
