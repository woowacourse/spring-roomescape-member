package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.dao.ReservationDao;
import roomescape.dao.TimeDao;
import roomescape.domain.ReservationTime;
import roomescape.dto.AvailableTimeResponse;
import roomescape.dto.TimeCreateRequest;
import roomescape.dto.TimeResponse;

import java.time.format.DateTimeFormatter;
import java.util.List;

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
        List<ReservationTime> alreadyBookedTime = timeDao.readTimesExistsReservationDateAndThemeId(date, themeId);

        return allTime.stream()
                .map(time -> AvailableTimeResponse.of(time, alreadyBookedTime.contains(time)))
                .toList();
    }

    public TimeResponse createTime(TimeCreateRequest dto) {
        if (timeDao.isExistTimeByStartAt(DateTimeFormatter.ofPattern("HH:mm").format(dto.startAt()))) {
            throw new IllegalArgumentException("해당 시간은 이미 존재합니다.");
        }
        ReservationTime createdTime = timeDao.createTime(dto.createReservationTime());
        return TimeResponse.from(createdTime);
    }

    public void deleteTime(Long id) {
        if (reservationDao.isExistReservationByTimeId(id)) {
            throw new IllegalArgumentException("해당 시간을 사용하는 예약이 존재합니다.");
        }
        timeDao.deleteTime(id);
    }
}
