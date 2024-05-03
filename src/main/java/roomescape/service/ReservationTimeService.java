package roomescape.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import roomescape.domain.ReservationTime;
import roomescape.dto.TimeMemberResponse;
import roomescape.dto.TimeResponse;
import roomescape.dto.TimeRequest;
import roomescape.exception.ExistReservationException;
import roomescape.exception.IllegalTimeException;
import roomescape.mapper.TimeMapper;
import roomescape.repository.ReservationDao;
import roomescape.repository.TimeDao;

@Service
public class ReservationTimeService {

    private final TimeMapper timeMapper = new TimeMapper();
    private final TimeDao timeDao;
    private final ReservationDao reservationDao;

    public ReservationTimeService(TimeDao timeDao, ReservationDao reservationDao) {
        this.timeDao = timeDao;
        this.reservationDao = reservationDao;
    }

    public List<TimeResponse> findAllTimes() {
        List<ReservationTime> reservationTimes = timeDao.findAll();
        return reservationTimes.stream()
                .map(timeMapper::mapToResponse)
                .toList();
    }

    public List<TimeMemberResponse> findAllTimesWithBooking(LocalDate date, Long themeId) {
        List<ReservationTime> allTimes = timeDao.findAll();
        List<TimeMemberResponse> allTimeResponsesWithBooking = new ArrayList<>();
        List<Long> bookedTimeIds = reservationDao.findTimeIdsByDateAndThemeId(date, themeId);

        for (ReservationTime time : allTimes) {
            boolean alreadyBooked = bookedTimeIds.contains(time.getId());
            allTimeResponsesWithBooking.add(timeMapper.mapToResponse(time, alreadyBooked));
        }

        return allTimeResponsesWithBooking;
    }

    public TimeResponse saveTime(TimeRequest request) {
        ReservationTime reservationTime = timeMapper.mapToTime(request);

        if (timeDao.existByTime(reservationTime.getStartAt())) {
            throw new IllegalTimeException("[ERROR] 중복된 시간은 생성할 수 없습니다.");
        }

        ReservationTime newReservationTime = timeDao.save(reservationTime);
        return timeMapper.mapToResponse(newReservationTime);
    }

    public void deleteTimeById(Long id) {
        try {
            timeDao.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new ExistReservationException("[ERROR] 예약이 존재하는 시간은 삭제할 수 없습니다.");
        }
    }
}
