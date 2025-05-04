package roomescape.reservation.repository;

import java.time.LocalDate;
import java.util.List;
import roomescape.reservation.domain.Reservation;

public interface ReservationRepository {
    List<Reservation> getAll();

    Reservation put(Reservation reservation);

    boolean deleteById(Long id);

    boolean existsByTimeId(Long id);

    boolean existsByThemeId(Long id);

    boolean existsByDateAndTimeId(final LocalDate date, final Long timeId);

    List<Reservation> findByDateAndThemeId(LocalDate date, Long themeId);
}
