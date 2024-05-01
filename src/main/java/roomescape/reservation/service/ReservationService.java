package roomescape.reservation.service;

import java.util.List;

import org.springframework.stereotype.Service;

import roomescape.reservation.domain.Reservation;
import roomescape.reservation.repository.ReservationDao;
import roomescape.reservation.request.ReservationRequest;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.ThemeDao;
import roomescape.time.domain.ReservationTime;
import roomescape.time.repository.ReservationTimeDao;

@Service
public class ReservationService {

    private final ReservationDao reservationDao;
    private final ReservationTimeDao reservationTimeDao;
    private final ThemeDao themeDao;

    public ReservationService(ReservationDao reservationDao, ReservationTimeDao reservationTimeDao, ThemeDao themeDao) {
        this.reservationDao = reservationDao;
        this.reservationTimeDao = reservationTimeDao;
        this.themeDao = themeDao;
    }

    public List<Reservation> findAll() {
        return reservationDao.findAll();
    }

    public Reservation save(ReservationRequest request) {
        if (reservationDao.existsByDateTime(request.date(), request.timeId())) {
            throw new IllegalArgumentException("Reservation already exists");
        }
        ReservationTime reservationTime = reservationTimeDao.findById(request.timeId());
        Theme theme = themeDao.findById(request.themeId());
        return reservationDao.save(new Reservation(0, request.name(), request.date(), reservationTime, theme));
    }

    public void delete(long id) {
        reservationDao.deleteById(id);
    }
}
