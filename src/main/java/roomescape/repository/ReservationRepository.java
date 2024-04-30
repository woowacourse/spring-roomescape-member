package roomescape.repository;

import roomescape.domain.Reservation;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository {

    List<Reservation> findAll();

    Optional<Reservation> findById(Long id);

    boolean existsByTimeId(Long timeId);

    boolean existsByDateAndTimeId(Long timeId, LocalDate date);

    Reservation save(Reservation reservation);

    int deleteById(Long id);
}
