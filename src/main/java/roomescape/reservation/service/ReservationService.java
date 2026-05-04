package roomescape.reservation.service;

import roomescape.reservation.domain.Reservation;

import java.time.LocalDate;
import java.util.List;

public interface ReservationService {

    List<Reservation> getReservations();
    Reservation createReservation(String name, LocalDate date, Long timeId, Long themeId);
    void deleteReservation(Long id);
}
