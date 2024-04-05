package roomescape.reservation.service;

import java.time.LocalDate;
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
        Reservation reservation = fromRequest(reservationRequest);
        Reservation savedReservation = reservationRepository.saveReservation(reservation);
        return toResponse(savedReservation);
    }

    public List<ReservationResponse> findReservations() {
        List<Reservation> reservations = reservationRepository.findAllReservation();

        return reservations.stream()
                .map(this::toResponse)
                .toList();
    }

    public void removeReservations(long reservationId) {
        reservationRepository.deleteReservationById(reservationId);
    }

    private Reservation fromRequest(ReservationRequest reservationRequest) {
        String name = reservationRequest.name();
        LocalDate date = reservationRequest.date();
        return new Reservation(name, date, reservationRequest.timeId());
    }

    private ReservationResponse toResponse(Reservation reservation) {
        return new ReservationResponse(reservation.getId(), reservation.getName(), reservation.getDate(),
                reservation.getReservationTime());
    }
}
