package roomescape.reservation.dao;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import roomescape.reservation.domain.Reservation;

public interface ReservationDao {

    long insert(Reservation reservation);

    boolean existSameReservation(LocalDate date, long timeId, long themeId);

    List<Reservation> findAll();

    Optional<Reservation> findById(long id);

    List<Reservation> findByConditions(Long memberId, Long themeId, LocalDate from, LocalDate to);

    boolean existsByTimeId(long timeId);

    boolean existsByThemeId(long themeId);

    boolean deleteById(long id);
}
