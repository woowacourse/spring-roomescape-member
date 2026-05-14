package roomescape.dao;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import roomescape.domain.Reservation;

public interface ReservationDao {
    Reservation create(Reservation reservation);

    List<Reservation> findAll();

    Optional<Reservation> findById(Long id);

    List<Reservation> findAllByName(String name);

    void update(Reservation reservation);

    void delete(Long id);

    boolean existsByDateAndTimeIdAndThemeId(LocalDate date, Long timeId, Long themeId);

    boolean existsByDateAndTimeIdAndThemeIdExcludingId(LocalDate date, Long timeId, Long themeId, Long excludeId);

    boolean existsByTimeId(Long timeId);
}
