package roomescape.reservation.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import roomescape.reservation.entity.Reservation;

public interface ReservationRepository {

    Long save(String name, LocalDate date, Long timeId);

    Optional<Reservation> findById(Long id);

    List<Reservation> findAll();

    void deleteById(Long id);

}
