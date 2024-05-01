package roomescape.domain;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository {
    List<Reservation> findAll();

    Optional<Reservation> findById(Long id);

    boolean existsByDateAndTimeIdAndThemeId(LocalDate date, Long timeId, Long themeId);

    Reservation save(Reservation reservation);

    void delete(Reservation reservation);
}
