package roomescape.dao;

import java.time.LocalDate;
import java.util.List;
import roomescape.domain.Reservation;

public interface ReservationDao {
    Reservation create(Reservation reservation);

    List<Reservation> readAll();

    void delete(Long id);

    boolean existsBy(LocalDate date, Long timeId, Long themeId);

    boolean existsByTimeId(Long timeId);

    List<Reservation> findByName(String name);
}
