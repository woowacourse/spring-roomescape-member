package roomescape.domain.repository;

import java.time.LocalDate;
import java.util.List;
import roomescape.domain.Reservation;

public interface ReservationRepository {
    List<Reservation> findAll();

    Long save(Reservation reservation);

    void deleteById(Long id);

    boolean existsDuplicatedReservation(LocalDate date, Long timeId, Long themeId);
}
