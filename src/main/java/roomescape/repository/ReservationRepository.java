package roomescape.repository;

import java.time.LocalDate;
import java.util.List;
import roomescape.domain.Reservation;

public interface ReservationRepository {

    Reservation save(Reservation reservation);

    void delete(Long id);

    boolean existByDateAndTimeId(LocalDate date, Long timeId);

    List<Reservation> findAll();
}
