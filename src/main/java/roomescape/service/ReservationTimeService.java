package roomescape.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.stereotype.Service;

import roomescape.domain.ReservationTime;
import roomescape.exception.EntityExistsException;
import roomescape.exception.ForeignKeyViolationException;
import roomescape.repository.ReservationDao;
import roomescape.repository.ReservationTimeDao;

@Service
public class ReservationTimeService {

    private final ReservationTimeDao reservationTimeDao;
    private final ReservationDao reservationDao;

    public ReservationTimeService(ReservationTimeDao reservationTimeDao, ReservationDao reservationDao) {
        this.reservationTimeDao = reservationTimeDao;
        this.reservationDao = reservationDao;
    }

    public List<ReservationTime> getAll() {
        return reservationTimeDao.findAll();
    }

    public List<ReservationTime> getAvailableTimes(LocalDate date, long themeId) {
        return reservationTimeDao.findAvailableTimes(date, themeId);
    }

    public ReservationTime create(ReservationTime reservationTime) {
        requireStartAtNotAlreadyExists(reservationTime);
        return reservationTimeDao.save(reservationTime);
    }

    public void delete(long id) {
        requireNotReferred(id);
        requireExists(id);
        reservationTimeDao.delete(id);
    }

    public boolean existsByStartAt(LocalTime startAt) {
        return reservationTimeDao.existsByStartAt(startAt);
    }

    private void requireExists(long id) {
        if (!reservationTimeDao.existsById(id)) {
            throw new EntityExistsException("Reservation time with id " + id + " does not exists.");
        }
    }

    private void requireNotReferred(long id) {
        if (reservationDao.existsByTimeId(id)) {
            throw new ForeignKeyViolationException(
                    "Cannot delete a time with id " + id + " as being referred by reservation.");
        }
    }

    private void requireStartAtNotAlreadyExists(ReservationTime reservationTime) {
        LocalTime startAt = reservationTime.startAt();
        if (existsByStartAt(startAt)) {
            throw new EntityExistsException("Time " + startAt + " already exists.");
        }
    }
}
