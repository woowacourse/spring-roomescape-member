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
        requireStartAtNotExists(reservationTime);
        return reservationTimeDao.save(reservationTime);
    }

    public void delete(long id) {
        if (reservationDao.existsByTimeId(id)) {
            throw new IllegalArgumentException("Cannot delete a reservation that refers to that time");
        }
        reservationTimeDao.delete(id);
    }

    public boolean existsByStartAt(LocalTime startAt) {
        return reservationTimeDao.exists(startAt);
    }

    public List<ReservationTime> available(LocalDate parse, long themeId) {
        return reservationTimeDao.available(parse,themeId);
    }

    private void requireStartAtNotExists(ReservationTime reservationTime) {
        if (existsByStartAt(reservationTime.startAt())) {
            throw new IllegalArgumentException("StartAt already exists");
        }
    }
}
