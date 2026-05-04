package roomescape.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import roomescape.dto.ReservationRequest;
import roomescape.dto.ReservationResponse;
import roomescape.model.Reservation;
import roomescape.repository.ReservationRepository;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;

    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public List<ReservationResponse> readReservation() {
        List<Reservation> reservations = reservationRepository.findAll();
        return reservations.stream()
                .map(ReservationResponse::from)
                .collect(Collectors.toList());
    }

    public void removeReservation(Long id) {
        reservationRepository.removeById(id);
    }

    public ReservationResponse registerReservation(ReservationRequest reservationRequest) {
        Reservation reservation = reservationRepository.register(reservationRequest.getName(),
                reservationRequest.getDate(), reservationRequest.getTimeId());
        return ReservationResponse.from(reservation);
    }

}
