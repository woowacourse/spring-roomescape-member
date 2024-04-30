package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.dto.ReservationRequest;
import roomescape.dto.ReservationResponse;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository timeRepository;

    public ReservationService(ReservationRepository reservationRepository, ReservationTimeRepository timeRepository) {
        this.reservationRepository = reservationRepository;
        this.timeRepository = timeRepository;
    }

    public Long addReservation(ReservationRequest reservationRequest) {
        ReservationTime reservationTime = findReservationTime(reservationRequest);
        validateReservationNotDuplicate(reservationRequest);
        Reservation reservationToSave = reservationRequest.toEntity(reservationTime);
        return reservationRepository.save(reservationToSave);
    }

    public List<ReservationResponse> getAllReservations() {
        List<Reservation> reservations = reservationRepository.findAll();
        return reservations.stream()
                .map(ReservationResponse::from)
                .toList();
    }

    public ReservationResponse getReservation(Long id) {
        validateIdExist(id);
        Reservation reservation = reservationRepository.findById(id);
        return ReservationResponse.from(reservation);
    }

    public void deleteReservation(Long id) {
        validateIdExist(id);
        reservationRepository.delete(id);
    }

    public void validateIdExist(Long id) {
        if (!reservationRepository.existId(id)) {
            throw new IllegalArgumentException("[ERROR] id가 존재하지 않습니다 : " + id);
        }
    }

    private ReservationTime findReservationTime(ReservationRequest reservationRequest) {
        Long timeId = reservationRequest.timeId();
        if (!timeRepository.existId(timeId)) {
            throw new IllegalArgumentException("[ERROR] time_id가 존재하지 않습니다 : " + timeId);
        }
        return timeRepository.findById(timeId);
    }

    private void validateReservationNotDuplicate(ReservationRequest reservationRequest) {
        if (reservationRepository.existDateAndTimeId(reservationRequest.date(), reservationRequest.timeId())) {
            throw new IllegalArgumentException("[ERROR] 이미 해당 시간에 예약이 존재합니다.");
        }
    }
}
