package roomescape.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import roomescape.domain.ReservationTime;
import roomescape.dto.TimeMemberResponse;
import roomescape.dto.TimeCreateRequest;
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
        List<TimeMemberResponse> allTimeResponsesWithBooking = new ArrayList<>();
        List<Long> bookedTimeIds = reservationDao.findTimeIdsByDateAndThemeId(date, themeId);

        for (ReservationTime time : allTimes) {
            boolean alreadyBooked = bookedTimeIds.contains(time.getId());
            allTimeResponsesWithBooking.add(TimeMemberResponse.of(time, alreadyBooked));
        }

        return allTimeResponsesWithBooking;
    }

    public TimeResponse save(TimeCreateRequest request) {
        ReservationTime reservationTime = TimeCreateRequest.toTime(request);

        if (timeDao.existByTime(reservationTime.getStartAt())) {
            throw new IllegalTimeException("[ERROR] 중복된 시간은 생성할 수 없습니다.");
        }

        ReservationTime newReservationTime = timeDao.save(reservationTime);
        return TimeResponse.fromTime(newReservationTime);
    }

    public void deleteTimeById(Long id) {
        try {
            timeDao.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new ExistReservationException("[ERROR] 예약이 존재하는 시간은 삭제할 수 없습니다.");
        }
    }
}
