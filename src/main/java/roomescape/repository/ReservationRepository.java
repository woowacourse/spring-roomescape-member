package roomescape.repository;

import roomescape.domain.Reservation;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository {

    List<Reservation> findAll();

    List<Reservation> findByDateAndThemeId(LocalDate date, Long id);

    Reservation save(Reservation reservation);

    int deleteById(Long id);
}
