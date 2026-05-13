package roomescape.repository;

import roomescape.domain.Reservation;
import roomescape.repository.dto.ReservationTimesWithStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository {

    List<Reservation> findAll();

    Optional<Reservation> findById(Long reservationId);

    Reservation save(Reservation newReservation);

    void deleteById(Long reservationId);

    List<ReservationTimesWithStatus> findReservationTimeStatusesByDateAndThemeId(LocalDate date, Long themeId);
}
