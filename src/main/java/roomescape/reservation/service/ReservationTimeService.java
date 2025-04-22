package roomescape.reservation.service;

import org.springframework.stereotype.Service;
import roomescape.reservation.dao.ReservationTimeDao;
import roomescape.reservation.dto.ReservationTimeRequest;
import roomescape.reservation.dto.ReservationTimeResponse;
import roomescape.reservation.entity.ReservationTime;

@Service
public class ReservationTimeService {
    private ReservationTimeDao reservationTimeDao;

    public ReservationTimeService(ReservationTimeDao reservationTimeDao) {
        this.reservationTimeDao = reservationTimeDao;
    }

    public ReservationTimeResponse createReservationTime(ReservationTimeRequest reservationTimeRequest) {
        ReservationTime reservationTime = new ReservationTime(
                null,
                reservationTimeRequest.getStartAt()
        );

        return new ReservationTimeResponse(reservationTimeDao.insert(reservationTime));
    }
}
