package roomescape.reservation.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.reservation.entity.Reservation;
import roomescape.reservation.payload.ReservationRequest;
import roomescape.reservation.repository.ReservationRepository;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;

    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @Transactional
    public Reservation save(ReservationRequest request) {
        Long id = reservationRepository.save(
                request.name(),
                request.date(),
                request.timeId(),
                request.themeId()
        );
        return reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("예약 저장 후 조회에 실패했습니다. id=" + id));
    }

    @Transactional(readOnly = true)
    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    @Transactional
    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }

}
