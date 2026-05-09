package roomescape.repository;

import roomescape.domain.Reservation;
import roomescape.repository.dto.ReservationTimesWithStatus;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository {

    List<Reservation> findAll();

    Reservation save(Reservation newReservation);

    boolean deleteById(Long reservationId);

    List<ReservationTimesWithStatus> findReservationTimeStatusesByDateAndThemeId(LocalDate date, Long themeId);
}
