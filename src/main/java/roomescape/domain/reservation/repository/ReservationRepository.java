package roomescape.domain.reservation.repository;

import org.springframework.stereotype.Repository;
import roomescape.domain.reservation.domain.Reservation;
import roomescape.domain.reservation.domain.Reservations;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository {

    List<Reservation> findAll(int offset, int limit);

    long count();

    Reservation save(Reservation reservation);

    void deleteById(Long id);

    boolean existsByTimeId(Long timeId);

    boolean existsByThemeId(Long themeId);

    Optional<Reservation> findById(Long id);

    Reservations findOn(LocalDate date, Long themeId);
}
