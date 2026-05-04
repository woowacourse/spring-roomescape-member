package roomescape.reservation.infra;

import java.time.LocalDate;
import java.util.List;
import roomescape.reservation.domain.Reservation;

public interface ReservationRepository {
    Reservation save(String name, LocalDate date, Long timeId);

    List<Reservation> findAll();

    void deleteById(Long id);
}
