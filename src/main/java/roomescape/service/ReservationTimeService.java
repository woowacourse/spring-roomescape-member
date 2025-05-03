package roomescape.service;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.domain.ReservationTime;
import roomescape.dto.request.ReservationTimeRequest;
import roomescape.dto.response.ReservationAvailableTimeResponse;
import roomescape.dto.response.ReservationTimeResponse;
import roomescape.exception.ForeignKeyConstraintViolationException;
import roomescape.exception.ResourceNotExistException;

import java.time.LocalDate;
import java.util.List;

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

    public void deleteReservationTime(Long id) {
        validateIsInUse(id);
        int deleteCount = reservationTimeDao.deleteById(id);
        if (deleteCount == 0) {
            throw new ResourceNotExistException();
        }
    }

    private void validateIsInUse(final Long id) {
        if (reservationDao.getCountByTimeId(id) != 0) {
            throw new ForeignKeyConstraintViolationException();
        }
    }

    public List<ReservationAvailableTimeResponse> findAvailableTimes(Long themeId, LocalDate date) {
        List<ReservationTime> times = reservationTimeDao.findAll();

        return times.stream()
                .map(time -> {
                    Long timeId = time.getId();
                    boolean isBooked = (reservationDao.getCountByTimeIdAndThemeIdAndDate(timeId, themeId, date) != 0);
                    return ReservationAvailableTimeResponse.from(time, isBooked);
                }).toList();
    }
}
