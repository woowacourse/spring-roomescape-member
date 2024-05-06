package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.dao.ReservationDao;
import roomescape.dao.TimeDao;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.time.Time;
import roomescape.dto.time.TimeRequest;
import roomescape.dto.time.TimeResponse;
import roomescape.dto.time.TimesResponse;
import roomescape.global.exception.error.ErrorType;
import roomescape.global.exception.model.DataConflictException;

import java.util.List;

@Service
public class TimeService {

    private final TimeDao timeDao;
    private final ReservationDao reservationDao;

    public TimeService(final TimeDao timeDao, final ReservationDao reservationDao) {
        this.timeDao = timeDao;
        this.reservationDao = reservationDao;
    }

    public TimesResponse findAllTimes() {
        List<TimeResponse> response = timeDao.findAll()
                .stream()
                .map(TimeResponse::from)
                .toList();

        return new TimesResponse(response);
    }

    public TimeResponse addTime(final TimeRequest timeRequest) {
        List<Time> duplicateTimes = timeDao.findByStartAt(timeRequest.startAt());
        if (duplicateTimes.size() > 0) {
            throw new DataConflictException(ErrorType.TIME_DUPLICATION_CONFLICT, "이미 존재하는 예약 시간입니다.");
        }

        Time time = timeRequest.toTime();
        Time savedTime = timeDao.insert(time);

        return TimeResponse.from(savedTime);
    }

    public void removeTimeById(final Long id) {
        List<Reservation> usingTimeReservations = reservationDao.findByTimeId(id);
        if (usingTimeReservations.size() > 0) {
            throw new DataConflictException(ErrorType.TIME_IS_USED_CONFLICT, String.format("[TimeId - %d] 해당 시간에 예약이 존재하여 시간을 삭제할 수 없습니다.", id));
        }
        timeDao.deleteById(id);
    }
}
