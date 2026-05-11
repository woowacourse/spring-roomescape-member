package roomescape.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.dao.ReservationTimeDao;
import roomescape.domain.ReservationTime;
import roomescape.dto.request.ReservationTimeCreateRequest;
import roomescape.dto.response.AvailableTimeResponse;
import roomescape.dto.response.ReservationTimeResponse;
import roomescape.exception.ReservationTimeInUseException;
import roomescape.exception.ReservationTimeNotFoundException;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class ReservationTimeService {

    private final ReservationTimeDao reservationTimeDao;

    public ReservationTimeService(ReservationTimeDao reservationTimeDao) {
        this.reservationTimeDao = reservationTimeDao;
    }

    public List<ReservationTimeResponse> getReservationTimes() {
        return ReservationTimeResponse.from(reservationTimeDao.findAllReservationTimes());
    }

    @Transactional
    public ReservationTimeResponse createReservationTime(ReservationTimeCreateRequest request) {
        Long id = reservationTimeDao.insertReservationTime(request.startAt());
        return ReservationTimeResponse.from(reservationTimeDao.findById(id));
    }

    @Transactional
    public void deleteReservationTime(Long id) {
        try {
            int deleteCount = reservationTimeDao.delete(id);
            validateDelete(deleteCount);
        } catch (DataIntegrityViolationException e) {
            throw new ReservationTimeInUseException();
        }
    }

    private void validateDelete(int deleteCount) {
        if (deleteCount == 0) {
            throw new ReservationTimeNotFoundException();
        }
    }

    public List<AvailableTimeResponse> getAvailableTimes(LocalDate date, Long id) {
        Map<ReservationTime, Boolean> reservationTimeBooleanMap = reservationTimeDao.findAvailableTimes(date, id);
        return AvailableTimeResponse.from(reservationTimeBooleanMap);
    }
}
