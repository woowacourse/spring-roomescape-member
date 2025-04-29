package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.dto.ReservationRequest;
import roomescape.dto.ReservationResponse;

@Service
public class ReservationService {

    private final ReservationDao reservationDao;
    private final ReservationTimeDao reservationTimeDao;

    public ReservationService(ReservationDao reservationDao,
        ReservationTimeDao reservationTimeDao) {
        this.reservationDao = reservationDao;
        this.reservationTimeDao = reservationTimeDao;
    }

    public ReservationResponse addReservation(ReservationRequest reservationRequest) {
        ReservationTime time = reservationTimeDao.findTimeById(reservationRequest.timeId());
        Reservation reservation = reservationDao.addReservation(
            ReservationRequest.toEntity(reservationRequest, time));

        return ReservationResponse.from(reservation);
    }

    public List<ReservationResponse> findAllReservations() {
        return reservationDao.findAllReservations().stream()
            .map(ReservationResponse::from)
            .toList();
    }

    public void removeReservation(Long id) {
        reservationDao.removeReservationById(id);
    }
}
