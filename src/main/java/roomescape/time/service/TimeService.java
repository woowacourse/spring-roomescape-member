package roomescape.time.service;

import java.time.LocalTime;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.exception.model.RoomEscapeException;
import roomescape.reservation.dao.ReservationDao;
import roomescape.time.dao.TimeDao;
import roomescape.time.domain.Time;
import roomescape.time.dto.TimeRequest;
import roomescape.time.dto.TimeResponse;
import roomescape.time.exception.TimeExceptionCode;

@Service
public class TimeService {
    private final TimeDao timeDao;
    private final ReservationDao reservationDao;

    public TimeService(TimeDao timeDao, ReservationDao reservationDao) {
        this.timeDao = timeDao;
        this.reservationDao = reservationDao;
    }

    public TimeResponse addReservationTime(TimeRequest timeRequest) {
        validateDuplicateTime(timeRequest.startAt());
        Time reservationTime = new Time(timeRequest.startAt());
        Time savedReservationTime = timeDao.save(reservationTime);

        return toResponse(savedReservationTime);
    }

    public List<TimeResponse> findReservationTimes() {
        List<Time> reservationTimes = timeDao.findAllReservationTimesInOrder();

        return reservationTimes.stream()
                .map(this::toResponse)
                .toList();
    }

    public void removeReservationTime(long reservationTimeId) {
        validateReservationExistence(reservationTimeId);
        timeDao.deleteById(reservationTimeId);
    }

    public TimeResponse toResponse(Time time) {
        return new TimeResponse(time.getId(), time.getStartAt());
    }

    public void validateDuplicateTime(LocalTime startAt) {
        int duplicateTimeCount = timeDao.countByStartAt(startAt);
        if (duplicateTimeCount > 0) {
            throw new RoomEscapeException(TimeExceptionCode.DUPLICATE_TIME_EXCEPTION);
        }
    }

    public void validateReservationExistence(long timeId) {
        int reservationCount = reservationDao.countByTimeId(timeId);
        if (reservationCount > 0) {
            throw new RoomEscapeException(TimeExceptionCode.EXIST_RESERVATION_AT_CHOOSE_TIME);
        }
    }
}
