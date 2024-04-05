package roomescape.reservation.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.reservation.dao.ReservationTimeDao;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.dto.request.ReservationTimeRequest;
import roomescape.reservation.dto.response.ReservationTimeResponse;

@Service
public class ReservationTimeService {
    private final ReservationTimeDao reservationTimeDao;

    public ReservationTimeService(ReservationTimeDao reservationTimeDao) {
        this.reservationTimeDao = reservationTimeDao;
    }

    public ReservationTimeResponse createReservationTime(ReservationTimeRequest reservationTimeRequest) {
        ReservationTime reservationTime = reservationTimeRequest.toEntity();

        ReservationTime savedReservationTime = reservationTimeDao.save(reservationTime);
        return ReservationTimeResponse.from(savedReservationTime);
    }

    public List<ReservationTimeResponse> findAllReservationTimes() {
        List<ReservationTime> reservationTimes = reservationTimeDao.findAllReservationTimes();
        return reservationTimes.stream()
                .map(ReservationTimeResponse::from)
                .toList();
    }

    public void deleteReservationTime(Long id) {
        reservationTimeDao.delete(id);
    }
}
