package roomescape.repository;

import java.time.LocalDate;
import java.util.List;
import roomescape.domain.Reservation;

public interface ReservationRepository {

    List<Reservation> findAll();

    List<Reservation> findByDateAndThemeId(LocalDate date, Long id);

    Reservation save(Reservation reservation);

    int deleteById(Long id);
}
