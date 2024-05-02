package roomescape.repository;

import roomescape.domain.Reservation;
import roomescape.domain.Theme;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository {

    List<Reservation> findAll();

    List<Reservation> findAllByDateAndThemeId(LocalDate date, Long themeId);

    Optional<Reservation> findById(Long id);

    boolean existsByTimeId(Long timeId);

    boolean existsByDateAndTimeId(Long timeId, LocalDate date);

    boolean existsByThemeId(Long themeId);

    Reservation save(Reservation reservation);

    int deleteById(Long id);

    List<Theme> findPopularThemes(LocalDate today);
}
