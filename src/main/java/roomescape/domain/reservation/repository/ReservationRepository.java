package roomescape.domain.reservation.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import roomescape.domain.reservation.entity.Reservation;

public interface ReservationRepository {

    List<Reservation> findAll();

    List<Reservation> findAllByUsername(String username);

    Optional<Reservation> findById(Long id);

    Reservation save(Reservation reservation);

    void update(Reservation reservation);

    void deleteById(Long id);

    boolean existsByThemeIdAndDateAndTimeId(Long themeId, LocalDate date, Long timeId);
}
