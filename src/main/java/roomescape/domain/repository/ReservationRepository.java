package roomescape.domain.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import roomescape.domain.Reservation;

public interface ReservationRepository {
    List<Reservation> findAll();

    Reservation create(Reservation reservation);

    void delete(Long id);

    List<Reservation> findByTimeId(Long id);

    Optional<Reservation> findById(Long id);

    Optional<Reservation> findByDateTime(LocalDate date, LocalTime time);
}

