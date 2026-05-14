package roomescape.reservation.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import roomescape.reservation.entity.Reservation;
import roomescape.reservationtime.entity.ReservationTime;

public interface ReservationRepository {

    Reservation save(Reservation reservation);

    Reservation update(Reservation reservation, LocalDate localDate, ReservationTime reservationTime);

    Optional<Reservation> findById(Long id);

    List<Reservation> findAll();

    List<Reservation> findAllByName(String name);

    int deleteById(Long id);

    int deleteByName(String name);

}
