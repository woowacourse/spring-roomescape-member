package roomescape.reservation.repository;

import java.time.LocalDate;
import java.util.List;
import roomescape.reservation.domain.Reservation;

public interface ReservationRepository {
    List<Reservation> findAll();

    Reservation save(Reservation reservation);

    List<Long> findTimeIdsByDate(LocalDate date);

    boolean deleteById(Long id);
}