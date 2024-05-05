package roomescape.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.stereotype.Service;

import roomescape.domain.RankTheme;
import roomescape.domain.Reservation;
import roomescape.repository.ReservationDao;
import roomescape.controller.request.ReservationRequest;
import roomescape.domain.Theme;
import roomescape.repository.ThemeDao;
import roomescape.domain.ReservationTime;
import roomescape.repository.ReservationTimeDao;

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
        if (reservationDao.existsByDateTime(request.date(), request.timeId(),request.themeId())) {
            throw new IllegalArgumentException("Reservation already exists");
        }
        ReservationTime reservationTime = reservationTimeDao.findById(request.timeId());
        Theme theme = themeDao.findById(request.themeId());
        return reservationDao.save(new Reservation(0, request.name(), request.date(), reservationTime, theme));
    }

    public Reservation validateFutureAndSave(ReservationRequest request) {
        if (reservationDao.existsByDateTime(request.date(), request.timeId(),request.themeId())) {
            throw new IllegalArgumentException("Reservation already exists");
        }
        ReservationTime reservationTime = reservationTimeDao.findById(request.timeId());
        validateDateTime(request.date(), reservationTime.startAt());
        Theme theme = themeDao.findById(request.themeId());
        return reservationDao.save(new Reservation(0, request.name(), request.date(), reservationTime, theme));

    }

    public void delete(long id) {
        reservationDao.deleteById(id);
    }

    public List<RankTheme> getRanking(){
        return themeDao.getRanking();
    }

    private void validateDateTime(LocalDate date, LocalTime time) {
        LocalDateTime dateTime = LocalDateTime.of(date, time);
        if (dateTime.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Cannot create a reservation for a past date and time.");
        }
    }
}
