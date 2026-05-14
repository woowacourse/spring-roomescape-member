package roomescape.repository;

import roomescape.command.ReservationEditCommand;
import roomescape.domain.Reservation;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository {

    Optional<Reservation> findById(Long id);

    List<Reservation> findAllReservations();

    Reservation addReservation(Reservation reservation);

    void deleteById(Long id);

    void updateCancelled(Long id);

    List<Reservation> findReservationsByName(String name);

    int countReservationsOf(LocalDate date, long timeId, long themeId);

    void updateReservation(Long id, ReservationEditCommand command);
}
