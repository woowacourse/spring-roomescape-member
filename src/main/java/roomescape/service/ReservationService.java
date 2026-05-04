package roomescape.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.Reservation;
import roomescape.dto.ReservationCreateResponse;
import roomescape.dto.ReservationResponse;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationService(ReservationRepository reservationRepository, ReservationTimeRepository reservationTimeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public List<ReservationResponse> getReservations() {
        return reservationRepository.findAll().stream()
                .map(ReservationResponse::new)
                .toList();
    }

    public boolean hasReservationsByTimeId(Long timeId) {
        return reservationRepository.existsByTimeId(timeId);
    }

    @Transactional
    public ReservationCreateResponse addReservation(Reservation reservation) {
        if (!reservationTimeRepository.existsById(reservation.getTime().getId())) {
            throw new IllegalArgumentException("존재하지 않는 시간 ID입니다.");
        }
        Long id = reservationRepository.save(reservation);
        return new ReservationCreateResponse(id);
    }

    @Transactional
    public void deleteReservation(Long id) {
        reservationRepository.deleteById(id);
    }
}
