package roomescape.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.stereotype.Service;

import roomescape.controller.request.ReservationRequest;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.exception.EntityExistsException;
import roomescape.exception.EntityNotFoundException;
import roomescape.repository.ReservationDao;
import roomescape.repository.ReservationTimeDao;
import roomescape.repository.ThemeDao;

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

    public List<Reservation> getAll() {
        return reservationDao.getAll();
    }

    public Reservation create(ReservationRequest request) {
        requireExistsByAttributes(request);
        ReservationTime reservationTime = reservationTimeDao.findById(request.timeId());
        Theme theme = themeDao.findById(request.themeId());
        return reservationDao.save(new Reservation(0, request.name(), request.date(), reservationTime, theme));
    }

    public Reservation createByMember(ReservationRequest request) {
        requireExistsByAttributes(request);
        ReservationTime reservationTime = reservationTimeDao.findById(request.timeId());
        requireFuture(request.date(), reservationTime.startAt());
        Theme theme = themeDao.findById(request.themeId());
        return reservationDao.save(new Reservation(0, request.name(), request.date(), reservationTime, theme));
    }

    public void delete(long id) {
        requireExistsById(id);
        reservationDao.deleteById(id);
    }

    private void requireExistsById(long id) {
        if (!reservationDao.existsById(id)) {
            throw new EntityNotFoundException("Reservation with id + " + id + " does not exists.");
        }
    }

    private void requireExistsByAttributes(ReservationRequest request) {
        if (reservationDao.existsByAttributes(request.date(), request.timeId(), request.themeId())) {
            throw new EntityExistsException("Reservation on " + request.date()
                    + " with timeId " + request.timeId()
                    + " and themeId " + request.themeId()
                    + " already exists.");
        }
    }

    private void requireFuture(LocalDate date, LocalTime time) {
        LocalDateTime dateTime = LocalDateTime.of(date, time);
        if (dateTime.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Cannot create a reservation for a past date and time.");
        }
    }
}
