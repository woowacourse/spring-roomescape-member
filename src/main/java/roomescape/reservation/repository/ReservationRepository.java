package roomescape.reservation.repository;

import java.time.LocalDate;
import java.util.List;
import roomescape.reservation.entity.Reservation;

public interface ReservationRepository {

    Reservation save(Reservation reservation);

    List<Reservation> findAll();

    List<Reservation> findAllByTimeId(Long id);

    boolean deleteById(Long id);

    boolean existsByDateAndTimeId(LocalDate date, Long timeId);
}
