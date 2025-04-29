package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dao.ReservationTimeDao;
import roomescape.domain.ReservationTime;
import roomescape.dto.TimeRequest;
import roomescape.dto.TimeResponse;
import roomescape.exception.DuplicateTimeException;

@Service
public class TimeService {

    private final ReservationTimeDao reservationTimeDao;

    public TimeService(ReservationTimeDao reservationTimeDao) {
        this.reservationTimeDao = reservationTimeDao;
    }

    public TimeResponse addReservationTime(TimeRequest timeRequest) {
        validateExistedTime(timeRequest);

        ReservationTime time = reservationTimeDao.addTime(TimeRequest.toEntity(timeRequest));
        return TimeResponse.from(time);
    }

    private void validateExistedTime(TimeRequest timeRequest) {
        if(reservationTimeDao.existTimeByStartAt(timeRequest.startAt())) {
            throw new DuplicateTimeException();
        }
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
