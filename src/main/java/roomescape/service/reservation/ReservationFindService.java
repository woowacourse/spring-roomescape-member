package roomescape.service.reservation;

import org.springframework.stereotype.Service;
import roomescape.domain.Reservation;
import roomescape.repository.ReservationRepository;

import java.util.List;

@Service
public class ReservationFindService {

    private final ReservationRepository reservationRepository;

    public ReservationFindService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public List<Reservation> findReservations() {
        return reservationRepository.findAll();
    }
}
