package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.controller.dto.ReservationRequest;
import roomescape.controller.dto.ReservationResponse;
import roomescape.repository.ReservationDao;
import roomescape.repository.ReservationTimeDao;
import roomescape.service.reservation.Reservation;
import roomescape.service.reservation.ReservationTime;

@Service
public class ReservationService {

    private final ReservationDao reservationDao;
    private final ReservationTimeDao reservationTimeDao;

    public ReservationService(final ReservationDao reservationDao, final ReservationTimeDao reservationTimeDao) {
        this.reservationDao = reservationDao;
        this.reservationTimeDao = reservationTimeDao;
    }

    public ReservationResponse createReservation(final ReservationRequest reservationRequest) {
        if (reservationDao.isExistsByDateAndTimeId(reservationRequest.date(), reservationRequest.timeId())) {
            throw new IllegalArgumentException("해당 시간에 이미 예약이 존재합니다.");
        }
        if (reservationTimeDao.isNotExistsById(reservationRequest.timeId())) {
            throw new IllegalArgumentException("예약 시간이 존재하지 않습니다.");
        }
        final ReservationTime reservationTime = reservationTimeDao.getReservationTimeById(reservationRequest.timeId());
        final Reservation convertedRequest = reservationRequest.convertToReservation(reservationTime);
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
