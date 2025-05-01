package roomescape.reservation.dao;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import roomescape.reservation.Reservation;

public interface ReservationDao {
    List<Reservation> findAll();

    Reservation create(Reservation reservation);

    void delete(Long id);

    Optional<Reservation> findByTimeId(Long id);

    Optional<Reservation> findById(Long id);

    Optional<Reservation> findByDateTime(LocalDate date, LocalTime time);
}

