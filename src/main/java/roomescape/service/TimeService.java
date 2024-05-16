package roomescape.service;

import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.stereotype.Service;

import roomescape.dao.ReservationDao;
import roomescape.dao.TimeDao;
import roomescape.domain.ReservationTime;
import roomescape.domain.exception.IllegalRequestArgumentException;
import roomescape.dto.request.TimeCreateRequest;
import roomescape.dto.response.AvailableTimeResponse;
import roomescape.dto.response.TimeResponse;

@Service
public class TimeService {
    private final TimeDao timeDao;
    private final ReservationDao reservationDao;

    public TimeService(TimeDao timeDao, ReservationDao reservationDao) {
        this.timeDao = timeDao;
        this.reservationDao = reservationDao;
    }

    public List<TimeResponse> readTimes() {
        return timeDao.readTimes().stream()
                .map(TimeResponse::from)
                .toList();
    }

    public List<AvailableTimeResponse> readAvailableTimes(String date, Long themeId) {
        List<ReservationTime> allTime = timeDao.readTimes();
        List<ReservationTime> alreadyBookedTime = timeDao.readTimesExistsReservation(date, themeId);

        return allTime.stream()
                .map(time -> AvailableTimeResponse.of(time, alreadyBookedTime.contains(time)))
                .toList();
    }

    public TimeResponse createTime(TimeCreateRequest dto) {
        if (timeDao.existsTimeByStartAt(DateTimeFormatter.ofPattern("HH:mm").format(dto.startAt()))) {
            throw new IllegalRequestArgumentException("해당 시간은 이미 존재합니다.");
        }
        ReservationTime createdTime = timeDao.createTime(dto.createReservationTime());
        return TimeResponse.from(createdTime);
    }

    public void deleteTime(Long id) {
        if (reservationDao.existsReservationByTimeId(id)) {
            throw new IllegalRequestArgumentException("해당 시간을 사용하는 예약이 존재합니다.");
        }
        timeDao.deleteTime(id);
    }
}
