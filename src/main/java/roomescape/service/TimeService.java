package roomescape.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dao.ReservationTimeDao;
import roomescape.domain.ReservationTime;
import roomescape.dto.TimeCreateRequest;
import roomescape.exception.DuplicateTimeException;
import roomescape.exception.InvalidInputException;

@Service
public class TimeService {

    private final ReservationTimeDao reservationTimeDao;

    public TimeService(ReservationTimeDao reservationTimeDao) {
        this.reservationTimeDao = reservationTimeDao;
    }

    public ReservationTime createReservationTime(TimeCreateRequest timeCreateRequest) {
        LocalTime startAt = timeCreateRequest.startAt();
        validateDuplicateStartAt(startAt);
        return reservationTimeDao.add(new ReservationTime(null, startAt));
    }

    private void validateDuplicateStartAt(LocalTime startAt) {
        if (reservationTimeDao.existByStartAt(startAt)) {
            throw new DuplicateTimeException();
        }
    }

    public List<ReservationTime> findAllReservationTimes() {
        return reservationTimeDao.findAll();
    }

    public List<ReservationTime> findReservationTimeByDateAndThemeIdWithIsBooked(LocalDate date, Long themeId) {
        return reservationTimeDao.findByDateAndThemeIdWithIsBooked(date, themeId);
    }

    public void deleteReservationTimeById(Long id) {
        if (reservationTimeDao.deleteById(id) == 0) {
            throw new InvalidInputException("존재하지 않는 예약시간 id이다.");
        }
    }
}
