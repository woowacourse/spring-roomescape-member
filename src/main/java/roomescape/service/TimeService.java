package roomescape.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dao.ReservationTimeDao;
import roomescape.domain.ReservationTime;
import roomescape.dto.TimeRequest;
import roomescape.exception.DuplicateTimeException;

@Service
public class TimeService {

    private final ReservationTimeDao reservationTimeDao;

    public TimeService(ReservationTimeDao reservationTimeDao) {
        this.reservationTimeDao = reservationTimeDao;
    }

    public ReservationTime addReservationTime(TimeRequest timeRequest) {
        validateExistedTime(timeRequest);
        return reservationTimeDao.addTime(new ReservationTime(null, timeRequest.startAt()));
    }

    private void validateExistedTime(TimeRequest timeRequest) {
        if(reservationTimeDao.existTimeByStartAt(timeRequest.startAt())) {
            throw new DuplicateTimeException();
        }
    }

    public List<ReservationTime> findAllReservationTimes() {
        return reservationTimeDao.findAllTimes();
    }

    public List<ReservationTime> findAllTimesWithBooked(LocalDate date, Long themeId) {
        return reservationTimeDao.findAllTimesWithBooked(date, themeId);
    }

    public void removeReservationTime(Long id) {
        reservationTimeDao.removeTimeById(id);
    }
}
