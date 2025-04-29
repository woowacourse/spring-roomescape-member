package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dao.ReservationTimeDao;
import roomescape.domain.ReservationTime;
import roomescape.dto.TimeRequest;
import roomescape.dto.TimeResponse;

@Service
public class TimeService {

    private final ReservationTimeDao reservationTimeDao;

    public TimeService(ReservationTimeDao reservationTimeDao) {
        this.reservationTimeDao = reservationTimeDao;
    }

    public TimeResponse addReservationTime(TimeRequest timeRequest) {
        ReservationTime time = reservationTimeDao.addTime(TimeRequest.toEntity(timeRequest));
        return TimeResponse.from(time);
    }

    public List<TimeResponse> findAllReservationTimes() {
        return reservationTimeDao.findAllTimes().stream()
            .map(TimeResponse::from)
            .toList();
    }

    public void removeReservationTime(Long id) {
        reservationTimeDao.removeTimeById(id);
    }
}
