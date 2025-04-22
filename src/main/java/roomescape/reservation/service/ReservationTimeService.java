package roomescape.reservation.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.reservation.dao.ReservationTimeDao;
import roomescape.reservation.dto.ReservationTimeRequest;
import roomescape.reservation.dto.ReservationTimeResponse;
import roomescape.reservation.model.ReservationTime;

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

    public List<ReservationTimeResponse> getReservationTimes() {
        return reservationTimeDao.findAllTimes().stream()
                .map(ReservationTimeResponse::new)
                .toList();
    }

    public void deleteReservationTime(Long id) {
        reservationTimeDao.delete(id);
    }
}
