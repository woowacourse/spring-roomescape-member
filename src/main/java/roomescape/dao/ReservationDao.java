package roomescape.dao;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import roomescape.domain.Reservation;

public interface ReservationDao {
    Reservation create(Reservation reservation);

    List<Reservation> readAll();

    void delete(Long id);

    boolean existsBy(LocalDate date, Long timeId, Long themeId);

    boolean existsByExceptId(LocalDate date, Long timeId, Long themeId, Long excludeId);

    boolean existsByTimeId(Long timeId);

    List<Reservation> findByName(String name);

    Optional<Reservation> findById(Long id);

    void update(Reservation reservation);
}
