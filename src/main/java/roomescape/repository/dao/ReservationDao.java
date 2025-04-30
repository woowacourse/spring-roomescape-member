package roomescape.repository.dao;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import roomescape.domain.Reservation;

public interface ReservationDao {

    List<Reservation> selectAll();

    Reservation insertAndGet(Reservation reservation);

    Optional<Reservation> selectById(Long id);

    void deleteById(Long id);

    boolean existDuplicatedDateTime(LocalDate date, Long timeId);
}
