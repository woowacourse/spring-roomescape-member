package roomescape.repository;

import org.springframework.stereotype.Repository;
import roomescape.domain.reservation.Reservation;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationDao {
    Reservation save(final Reservation reservation);

    Optional<Reservation> findById(long id);

    List<Reservation> getAll();

    List<Reservation> findByTimeId(final long timeId);

    List<Reservation> findByThemeId(final long themeId);

    // TODO Reservation으로 통일 시키기
    List<Long> findByDateAndTimeIdAndThemeId(LocalDate date, long timeId, long themeId);

    List<Long> findTimeIdByDateAndThemeId(LocalDate date, long themeId);

    List<Long> findRanking(final LocalDate from, final LocalDate to, final int count);

    void delete(final long id);
}
