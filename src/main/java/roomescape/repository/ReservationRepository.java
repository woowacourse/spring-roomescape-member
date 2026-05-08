package roomescape.repository;

import roomescape.domain.Reservation;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository {
    List<Reservation> findAll();

    List<Reservation> findByDateAndThemeId(LocalDate date, Long themeId);

    Reservation save(Reservation reservation);

    boolean existsByDateAndTimeIdAndThemeId(LocalDate date, Long timeId, Long themeId);

    void deleteById(Long id);
}
