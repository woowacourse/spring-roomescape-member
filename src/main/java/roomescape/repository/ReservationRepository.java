package roomescape.repository;

import org.springframework.stereotype.Repository;
import roomescape.model.Reservation;
import roomescape.model.ReservationTime;
import roomescape.model.Theme;
import roomescape.repository.dao.ReservationDao;
import roomescape.repository.dao.ReservationTimeDao;
import roomescape.repository.dao.ThemeDao;
import roomescape.repository.dto.ReservationSavedDto;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class ReservationRepository {

    private final ReservationDao reservationDao;
    private final ReservationTimeDao reservationTimeDao;
    private final ThemeDao themeDao;

    public ReservationRepository(ReservationDao reservationDao, ReservationTimeDao reservationTimeDao, ThemeDao themeDao) {
        this.reservationDao = reservationDao;
        this.reservationTimeDao = reservationTimeDao;
        this.themeDao = themeDao;
    }

    public List<Reservation> findAllReservations() {
        List<Reservation> result = new ArrayList<>();
        List<ReservationSavedDto> reservations = reservationDao.findAll();
        for (ReservationSavedDto reservation : reservations) {
            ReservationTime time = reservationTimeDao.findById(reservation.getTimeId()).orElseThrow(NoSuchElementException::new);
            Theme theme = themeDao.findById(reservation.getThemeId()).orElseThrow(NoSuchElementException::new);
            result.add(new Reservation(reservation.getId(), reservation.getName(), reservation.getDate(), time, theme));
        }
        return result;
    }

    public Optional<ReservationTime> findReservationTimeById(long id) {
        return reservationTimeDao.findById(id);
    }

    public Optional<Theme> findThemeById(long id) {
        return themeDao.findById(id);
    }

    public boolean isExistReservationByDateAndTimeId(LocalDate date, long timeId) {
        return reservationDao.isExistByDateAndTimeId(date, timeId);
    }

    public Reservation saveReservation(Reservation reservation) {
        long id = reservationDao.save(reservation);
        ReservationSavedDto saved = reservationDao.findById(id).orElseThrow(NoSuchElementException::new);
        ReservationTime time = reservationTimeDao.findById(saved.getTimeId()).orElseThrow(NoSuchElementException::new);
        Theme theme = themeDao.findById(saved.getThemeId()).orElseThrow(NoSuchElementException::new);
        return new Reservation(id, saved.getName(), saved.getDate(), time, theme);
    }

    public boolean isExistReservationById(long id) {
        return reservationDao.isExistById(id);
    }

    public void deleteReservationById(long id) {
        reservationDao.deleteById(id);
    }

    public List<ReservationTime> findReservationTimeBooked(LocalDate date, long themeId) {
        List<ReservationTime> result = new ArrayList<>();
        List<ReservationSavedDto> reservations = reservationDao.findByDateAndThemeId(date, themeId);
        Set<Long> timeIds = reservations.stream()
                .map(ReservationSavedDto::getTimeId)
                .collect(Collectors.toSet());
        for (long timeId : timeIds) {
            ReservationTime time = reservationTimeDao.findById(timeId).orElseThrow(NoSuchElementException::new);
            result.add(time);
        }
        return result;
    }

    public List<ReservationTime> findReservationTimeNotBooked(LocalDate date, long themeId) {
        List<ReservationTime> result = reservationTimeDao.findAll();
        List<ReservationTime> bookedTimes = findReservationTimeBooked(date, themeId);
        result.removeAll(bookedTimes);
        return result;
    }
}
