package roomescape.repository;

import org.springframework.cglib.core.Local;
import roomescape.domain.Reservation;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository {
    List<Reservation> findAllReservations();

    Reservation addReservation(Reservation reservation);

    void deleteById(Long id);

    List<Reservation> findReservationsByName(String name);

    int countReservationsOf(LocalDate date, long timeId, long themeId);
}
