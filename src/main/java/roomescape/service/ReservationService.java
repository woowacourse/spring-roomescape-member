package roomescape.service;

import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.dto.ReservationRequest;
import roomescape.dto.ReservationResponse;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;

@Service
@Transactional(readOnly = true)
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationService(ReservationRepository reservationRepository,
                              ReservationTimeRepository reservationTimeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public List<ReservationResponse> getAllReservations() {
        List<Reservation> reservations = reservationRepository.findAll();

        return reservations.stream()
                .map(ReservationResponse::from)
                .toList();
    }

    @Transactional
    public ReservationResponse addReservation(ReservationRequest reservationRequest) {
        ReservationTime reservationTime = reservationTimeRepository.getById(reservationRequest.timeId());
        Reservation reservation = reservationRequest.toReservation(reservationTime);

        if(reservationRepository.existsByDateAndTimeId(reservation.getDate(), reservation.getTimeId())) {
            throw new IllegalArgumentException("해당 날짜/시간에 이미 예약이 존재합니다.");
        }

        Reservation savedReservation = reservationRepository.save(reservation);

        return ReservationResponse.from(savedReservation);
    }

    @Transactional
    public void deleteReservationById(Long id) {
        reservationRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당 id의 예약이 존재하지 않습니다."));

        reservationRepository.deleteById(id);
    }
}
