package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.controller.dto.ReservationRequest;
import roomescape.controller.dto.ReservationResponse;
import roomescape.repository.ReservationDao;
import roomescape.service.reservation.Reservation;

@Service
public class ReservationService {

    private final ReservationDao reservationDao;

    public ReservationService(final ReservationDao reservationDao) {
        this.reservationDao = reservationDao;
    }

    public ReservationResponse createReservation(final ReservationRequest reservationRequest) {
        final Reservation convertedRequest = reservationRequest.convertToReservation();
        final Reservation reservation = reservationDao.createReservation(convertedRequest);
        return new ReservationResponse(reservation);
    }

    public List<ReservationResponse> getReservations() {
        return reservationDao.getReservations().stream()
                .map(ReservationResponse::new)
                .toList();
    }

    public void deleteReservation(final long id) {
        reservationDao.deleteReservationById(id);
    }
}
