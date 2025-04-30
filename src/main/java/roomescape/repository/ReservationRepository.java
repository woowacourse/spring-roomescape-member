package roomescape.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import roomescape.domain.Reservation;

public interface ReservationRepository {

    long add(Reservation reservation);

    List<Reservation> findAll();

    void deleteById(Long id);

    boolean existsByTimeId(Long id);

    boolean existsByDateAndTimeId(Reservation reservation);

    List<Reservation> findAllByDateAndThemeId(LocalDate date, Long themeId);

    Optional<Reservation> findById(long addedReservationId);
}
