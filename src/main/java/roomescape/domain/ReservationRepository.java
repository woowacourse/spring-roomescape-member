package roomescape.domain;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository {
    List<Reservation> findAll();

    Optional<Reservation> findById(Long id);

    Boolean existsByDateAndTimeId(LocalDate date, Long timeId);

    Reservation save(Reservation reservation);

    void delete(Reservation reservation);
}
