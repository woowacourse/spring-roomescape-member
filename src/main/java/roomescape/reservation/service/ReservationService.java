package roomescape.reservation.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.dto.ReservationRequest;
import roomescape.reservation.dto.ReservationResponse;
import roomescape.reservation.repository.ReservationRepository;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;

    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public ReservationResponse addReservation(ReservationRequest reservationRequest) {
        Reservation reservation = reservationRequest.fromRequest();
        Reservation savedReservation = reservationRepository.saveReservation(reservation);
        return ReservationResponse.fromReservation(savedReservation);
    }

    public List<ReservationResponse> findReservations() {
        List<Reservation> reservations = reservationRepository.findAllReservation();

        return reservations.stream()
                .map(ReservationResponse::fromReservation)
                .toList();
    }

    public void removeReservations(long reservationId) {
        reservationRepository.deleteReservationById(reservationId);
    }
}
