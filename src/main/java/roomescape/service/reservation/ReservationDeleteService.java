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
        reservationRepository.findAll()
                .stream()
                .filter(reservation -> reservation.isSameReservation(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 예약 아이디 입니다."));

        reservationRepository.deleteById(id);
    }
}
