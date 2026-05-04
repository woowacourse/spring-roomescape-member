package roomescape.service;

import java.util.List;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.dao.ReservationTimeDao;
import roomescape.domain.ReservationTime;
import roomescape.exception.ReservationTimeInUseException;
import roomescape.exception.ReservationTimeNotFoundException;

@Service
@Transactional(readOnly = true)
public class ReservationTimeService {

    private final ReservationTimeDao reservationTimeDao;

    public ReservationTimeService(ReservationTimeDao reservationTimeDao) {
        this.reservationTimeDao = reservationTimeDao;
    }

    public List<ReservationTime> getReservationTimes() {
        return reservationTimeDao.findAllReservationTimes();
    }

    @Transactional
    public ReservationTime createReservationTime(ReservationTime reservationTime) {
        Long id = reservationTimeDao.insertWithKeyHolder(reservationTime);
        return ReservationTime.withId(id, reservationTime);
    }

    @Transactional
    public void deleteReservationTime(Long id) {
        try {
            int deleteCount = reservationTimeDao.delete(id);
            if (deleteCount == 0) {
                throw new ReservationTimeNotFoundException();
            }
        } catch (DataIntegrityViolationException e) {
            throw new ReservationTimeInUseException();
        }
    }
}
