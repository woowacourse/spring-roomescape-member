package roomescape.service.reservationtime;

import org.springframework.stereotype.Service;
import roomescape.repository.ReservationTimeRepository;

@Service
public class ReservationTimeDeleteService {

    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationTimeDeleteService(ReservationTimeRepository reservationTimeRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public void deleteReservationTime(Long id) {
        reservationTimeRepository.findAll()
                .stream()
                .filter(reservationTime -> reservationTime.isSameReservationTime(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 예약 아이디 입니다."));

        reservationTimeRepository.deleteById(id);
    }
}
