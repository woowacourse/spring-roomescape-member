package roomescape.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.Reservation;
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

    public List<Reservation> getReservations() {
        return reservationRepository.findAll();
    }

    public boolean hasReservationsByTimeId(Long timeId) {
        return reservationRepository.existsByTimeId(timeId);
    }

    @Transactional
    public Reservation addReservation(Reservation reservation) {
        if (!reservationTimeRepository.existsById(reservation.getTime().getId())) {
            throw new IllegalArgumentException("존재하지 않는 시간 ID입니다.");
        }

        Long id = reservationRepository.save(reservation);
        return findById(id);
    }

    @Transactional
    public void deleteReservation(Long id) {
        reservationRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Reservation findById(Long id) {
        return reservationRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 예약 ID입니다."));
    }
}
