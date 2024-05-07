package roomescape.service;

import java.util.List;

import org.springframework.stereotype.Service;

import roomescape.controller.request.ReservationRequest;
import roomescape.exception.NotFoundException;
import roomescape.model.Reservation;
import roomescape.repository.ReservationRepositoryImpl;

@Service
public class ReservationService {

    private final ReservationRepositoryImpl reservationRepositoryImpl;

    public ReservationService(ReservationRepositoryImpl reservationRepositoryImpl) {
        this.reservationRepositoryImpl = reservationRepositoryImpl;
    }

    public List<Reservation> findAllReservations() {
        return reservationRepositoryImpl.getAllReservations();
    }

    public Reservation addReservation(ReservationRequest request) {
        return reservationRepositoryImpl.addReservation(request);
    }

    public void deleteReservation(long id) {
        validateExistReservation(id);
        reservationRepositoryImpl.deleteReservation(id);
    }

    private void validateExistReservation(long id) {
        Long count = reservationRepositoryImpl.countReservationById(id);
        if (count == null || count <= 0) {
            throw new NotFoundException("해당 id:[%s] 값으로 예약된 내역이 존재하지 않습니다.".formatted(id));
        }
    }
}
