package roomescape.service.reservation;

import org.springframework.stereotype.Service;
import roomescape.repository.ReservationRepository;

@Service
public class ReservationDeleteService {

    private final ReservationRepository reservationRepository;

    public ReservationDeleteService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public void deleteReservation(Long id) {
        reservationRepository.deleteById(id);
    }
}
