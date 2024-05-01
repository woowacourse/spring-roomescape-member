package roomescape.dao;

import java.time.LocalDate;
import java.util.List;
import roomescape.domain.Reservation;

public interface ReservationDao {
    List<Reservation> findAll();

    boolean existsByDateTime(LocalDate date, Long timeId);

    Reservation save(Reservation reservation);

    boolean deleteById(Long id);
}
