package roomescape.time.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.stereotype.Service;

import roomescape.reservation.repository.ReservationDao;
import roomescape.time.response.AvailableTime;
import roomescape.time.domain.ReservationTime;
import roomescape.time.repository.ReservationTimeDao;

@Service
public class ReservationTimeService {
    private static final int DELETE_SUCCESS = 1;
    private final ReservationTimeDao reservationTimeDao;
    private final ReservationDao reservationDao;

    public ReservationTimeService(ReservationTimeDao reservationTimeDao, ReservationDao reservationDao) {
        this.reservationTimeDao = reservationTimeDao;
        this.reservationDao = reservationDao;
    }

    public List<ReservationTime> findAll() {
        return reservationTimeDao.findAll();
    }

    public ReservationTime save(ReservationTime reservationTime) {
        if (exists(reservationTime.startAt())) {
            throw new IllegalArgumentException("StartAt already exists");
        }
        return reservationTimeDao.save(reservationTime);
    }

    public boolean exists(LocalTime startAt) {
        return reservationTimeDao.exists(startAt);
    }

    public void delete(long id) {
        if (reservationDao.existsTime(id)) {
            throw new IllegalArgumentException("Cannot delete a reservation that refers to that time");
        }
        if (reservationTimeDao.delete(id) != DELETE_SUCCESS) {
            throw new IllegalArgumentException("Cannot delete a reservationTime by given id");
        }
    }

    public List<AvailableTime> available(LocalDate date, long themeId) {
        return reservationTimeDao.available(date, themeId);
    }
}
