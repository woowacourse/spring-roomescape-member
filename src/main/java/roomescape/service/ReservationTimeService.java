package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.controller.dto.ReservationTimeRequest;
import roomescape.controller.dto.ReservationTimeResponse;
import roomescape.repository.ReservationTimeDao;
import roomescape.service.reservation.ReservationTime;

@Service
public class ReservationTimeService {

    private final ReservationTimeDao reservationTimeDao;

    public ReservationTimeService(final ReservationTimeDao reservationTimeDao) {
        this.reservationTimeDao = reservationTimeDao;
    }

    public ReservationTimeResponse createReservationTime(final ReservationTimeRequest reservationTimeRequest) {
        final ReservationTime convertedRequest = reservationTimeRequest.converToReservationTime();
        final ReservationTime reservationTime = reservationTimeDao.createReservationTime(convertedRequest);
        return new ReservationTimeResponse(reservationTime);
    }

    public List<ReservationTimeResponse> getReservationTimes() {
        return reservationTimeDao.getReservationTimes().stream()
                .map(ReservationTimeResponse::new)
                .toList();
    }

    public void deleteReservationTime(final long id) {
        reservationTimeDao.deleteReservationTimeById(id);
    }
}
