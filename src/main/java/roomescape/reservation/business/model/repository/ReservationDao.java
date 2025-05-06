package roomescape.reservation.business.model.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import roomescape.reservation.business.model.entity.Reservation;

public interface ReservationDao {

    List<Reservation> findAll();

    Reservation save(Reservation reservation);

    int deleteById(Long id);

    Optional<Reservation> findById(Long id);

    boolean existByTimeId(Long timeId);

    List<Reservation> findByDateAndThemeId(LocalDate date, Long themeId);

    List<Reservation> findByThemeIdAndMemberIdInDuration(long themeId, long memberId, LocalDate start, LocalDate end);
}
