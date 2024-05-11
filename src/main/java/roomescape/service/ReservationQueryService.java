package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Component;
import roomescape.infrastructure.persistence.ReservationRepository;
import roomescape.service.response.ReservationResponse;

@Component
public class ReservationQueryService {

    private final ReservationRepository reservationRepository;

    public ReservationQueryService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public List<ReservationResponse> getAllReservations() {
        return reservationRepository.findAll().stream()
                .map(ReservationResponse::from)
                .toList();
    }
}
