package roomescape.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import roomescape.dao.ReservationTimeDao;
import roomescape.domain.ReservationTime;
import roomescape.dto.ReservationTimeRequest;
import roomescape.dto.ReservationTimeResponse;

@Service
public class ReservationTimeService {
    private final ReservationTimeDao reservationTimeDao;

    public ReservationTimeService(ReservationTimeDao reservationTimeDao) {
        this.reservationTimeDao = reservationTimeDao;
    }

    public ReservationTimeResponse addReservationTime(ReservationTimeRequest request) {
        ReservationTime reservationTime = new ReservationTime(request.startAt());
        ReservationTime newReservationTime = reservationTimeDao.insert(reservationTime);
        return ReservationTimeResponse.from(newReservationTime);
    }

    public List<ReservationTimeResponse> getReservationTimes() {
        List<ReservationTime> reservationTimes = reservationTimeDao.select();
        return reservationTimes.stream()
                .map(ReservationTimeResponse::from)
                .collect(Collectors.toList());
    }

    public void deleteReservationTime(Long reservationTimeId) {
        reservationTimeDao.delete(reservationTimeId);
    }
}
