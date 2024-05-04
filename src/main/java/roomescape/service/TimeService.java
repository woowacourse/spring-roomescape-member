package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dao.TimeDao;
import roomescape.domain.ReservationTime;
import roomescape.dto.AvailableTimeResponse;
import roomescape.dto.TimeCreateRequest;
import roomescape.dto.TimeResponse;

@Service
public class TimeService {
    private final TimeDao timeDao;

    public TimeService(TimeDao timeDao) {
        this.timeDao = timeDao;
    }

    public List<TimeResponse> readTimes() {
        return timeDao.readTimes()
                .stream()
                .map(TimeResponse::from)
                .toList();
    }

    public List<AvailableTimeResponse> readAvailableTimes(LocalDate date, Long themeId) {
        List<ReservationTime> allTime = timeDao.readTimes();
        List<ReservationTime> alreadyBookedTime = timeDao.readTimesExistsReservationDateAndThemeId(date, themeId);

        return allTime.stream()
                .map(time -> AvailableTimeResponse.of(time, alreadyBookedTime.contains(time)))
                .toList();
    }

    public TimeResponse createTime(TimeCreateRequest dto) {
        if (timeDao.isExistTimeByStartAt(dto.startAt())) {
            throw new IllegalArgumentException("해당 시간은 이미 존재합니다.");
        }
        ReservationTime createdTime = timeDao.createTime(dto.createReservationTime());
        return TimeResponse.from(createdTime);
    }

    public void deleteTime(Long id) {
        timeDao.deleteTime(id);
    }
}
