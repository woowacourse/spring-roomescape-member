package roomescape.service;

import java.util.List;

import org.springframework.stereotype.Service;

import roomescape.controller.request.ReservationRequest;
import roomescape.exception.NotFoundException;
import roomescape.model.Reservation;
import roomescape.repository.ReservationRepository;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;

    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public List<Reservation> findAllReservations() {
        return reservationRepository.getAllReservations();
    }

    public Reservation addReservation(ReservationRequest request) {
        return reservationRepository.addReservation(request);
    }

    public void deleteReservation(long id) {
        validateExistReservation(id);
        reservationRepository.deleteReservation(id);
    }

    private void validateExistReservation(long id) {
        Long count = reservationRepository.countReservationById(id);
        if (count == null || count <= 0) {
            throw new NotFoundException("해당 id:[%s] 값으로 예약된 내역이 존재하지 않습니다.".formatted(id));
        }
    }
}
