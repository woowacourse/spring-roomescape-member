package roomescape.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.stereotype.Service;

import roomescape.domain.ReservationTime;
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
        return reservationTimeDao.getAll();
    }

    public ReservationTime create(ReservationTime reservationTime) {
        requireStartAtNotAlreadyExists(reservationTime);
        return reservationTimeDao.save(reservationTime);
    }

    public void delete(long id) {
        requireExistsById(id);
        reservationTimeDao.delete(id);
    }

    public boolean existsByStartAt(LocalTime startAt) {
        return reservationTimeDao.exists(startAt);
    }

    public List<ReservationTime> getAvailableTimes(LocalDate date, long themeId) {
        return reservationTimeDao.getAvailableTimes(date, themeId);
    }

    private void requireExistsById(long id) {
        if (reservationDao.existsByTimeId(id)) {
            throw new IllegalArgumentException("Cannot delete a reservation that refers to that time");
        }
    }

    private void requireStartAtNotAlreadyExists(ReservationTime reservationTime) {
        if (existsByStartAt(reservationTime.startAt())) {
            throw new IllegalArgumentException("StartAt already exists");
        }
    }
}
