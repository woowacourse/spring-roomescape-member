package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.domain.ReservationTime;
import roomescape.dto.request.ReservationTimeRequest;
import roomescape.dto.response.ReservationAvailableTimeResponse;
import roomescape.dto.response.ReservationTimeResponse;
import roomescape.exception.ResourceNotExistException;
import roomescape.exception.TimeConstraintException;

@Service
public class ReservationTimeService {

    private final ReservationTimeDao reservationTimeDao;
    private final ReservationDao reservationDao;

    public ReservationTimeService(
        ReservationTimeDao reservationTimeDao,
        ReservationDao reservationDao
    ) {
        this.reservationTimeDao = reservationTimeDao;
        this.reservationDao = reservationDao;
    }

    public ReservationTimeResponse save(ReservationTimeRequest request) {
        ReservationTime reservationTime = new ReservationTime(request.startAt());
        try {
            reservationTime = reservationTimeDao.save(reservationTime);
            return ReservationTimeResponse.from(reservationTime);
        } catch (DataAccessException e) {
            throw new IllegalArgumentException("[ERROR] 예약 시간 생성에 실패하였습니다");
        }
    }

    public List<ReservationTimeResponse> findAll() {
        return reservationTimeDao.findAll()
            .stream()
            .map(ReservationTimeResponse::from)
            .toList();
    }

    public List<ReservationAvailableTimeResponse> findAvailableTimes(Long themeId, LocalDate date) {
        List<ReservationTime> times = reservationTimeDao.findAll();
        List<Long> bookedTimeIds = reservationDao.findTimeIdsByThemeIdAndDate(themeId, date);
        return times.stream()
            .map(time -> {
                Long timeId = time.getId();
                boolean isBooked = bookedTimeIds.contains(timeId);
                return ReservationAvailableTimeResponse.from(time, isBooked);
            }).toList();
    }

    public void deleteReservationTime(Long id) {
        int deleteCount;
        try {
            deleteCount = reservationTimeDao.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new TimeConstraintException();
        }
        if (deleteCount == 0) {
            throw new ResourceNotExistException();
        }
    }
}
