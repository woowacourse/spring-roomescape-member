package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import roomescape.domain.ReservationTime;
import roomescape.dto.TimeCreateRequest;
import roomescape.dto.TimeMemberResponse;
import roomescape.dto.TimeResponse;
import roomescape.exception.ExistReservationException;
import roomescape.exception.IllegalTimeException;
import roomescape.repository.ReservationDao;
import roomescape.repository.TimeDao;

@Service
public class ReservationTimeService {

    private final TimeDao timeDao;
    private final ReservationDao reservationDao;

    public ReservationTimeService(TimeDao timeDao, ReservationDao reservationDao) {
        this.timeDao = timeDao;
        this.reservationDao = reservationDao;
    }

    public List<TimeResponse> findAll() {
        List<ReservationTime> reservationTimes = timeDao.findAll();
        return TimeResponse.fromTimes(reservationTimes);
    }

    public List<TimeMemberResponse> findAllWithBooking(LocalDate date, Long themeId) {
        List<ReservationTime> allTimes = timeDao.findAll();
        List<Long> bookedTimeIds = reservationDao.findTimeIdsByDateAndThemeId(date, themeId);

        return allTimes.stream()
                .map(time -> TimeMemberResponse.of(time, bookedTimeIds.contains(time.getId())))
                .toList();
    }

    public long save(TimeCreateRequest request) {
        ReservationTime reservationTime = TimeCreateRequest.toTime(request);

        if (timeDao.existByTime(reservationTime.getStartAt())) {
            throw new IllegalTimeException("[ERROR] 중복된 시간은 생성할 수 없습니다.");
        }

        return timeDao.save(reservationTime);
    }

    public void deleteTimeById(Long id) {
        try {
            timeDao.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new ExistReservationException("[ERROR] 예약이 존재하는 시간은 삭제할 수 없습니다.");
        }
    }
}
