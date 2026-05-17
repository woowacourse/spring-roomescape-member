package roomescape.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.exception.PastReservationCancelNotAllowedException;
import roomescape.exception.PastReservationNotAllowedException;
import roomescape.exception.ReservationAlreadyExistsException;
import roomescape.exception.ReservationNotFoundException;
import roomescape.exception.ReservationOwnerMismatchException;
import roomescape.exception.ReservationTimeNotFoundException;

@Service
@Transactional(readOnly = true)
public class ReservationService {

    private final ReservationDao reservationDao;
    private final ReservationTimeDao reservationTimeDao;

    public ReservationService(ReservationDao reservationDao, ReservationTimeDao reservationTimeDao) {
        this.reservationDao = reservationDao;
        this.reservationTimeDao = reservationTimeDao;
    }

    public List<Reservation> getReservations(String userName) {
        if (userName == null) {
            return reservationDao.findAllReservations();
        }
        return reservationDao.findAllReservationsByUserName(userName);
    }

    @Transactional
    public Reservation createReservation(String name, LocalDate date, Long timeId, Long themeId) {
        LocalTime startAt = reservationTimeDao.findReservationTimeById(timeId).getStartAt();
        validatePastReservationCreate(date, startAt);
        try {
            Long id = reservationDao.insertWithKeyHolder(name, date, timeId, themeId);
            return reservationDao.findReservationById(id);
        } catch (DuplicateKeyException e) {
            throw new ReservationAlreadyExistsException();
        }
    }

    @Transactional
    public void deleteReservation(Long id) {
        Reservation reservation = findReservation(id);
        validatePastReservationCancel(reservation.getDate(), reservation.getTime().getStartAt());
        reservationDao.delete(id);
    }

    @Transactional
    public Reservation updateReservation(Long id, LocalDate date, String name, Long timeId) {
        ReservationTime reservationTime = findReservationTime(timeId);
        validateReservationOwnerName(name, findReservation(id));
        validatePastReservationCreate(date, reservationTime.getStartAt());
        try {
            reservationDao.updateById(id, date, timeId);
        } catch (DuplicateKeyException e) {
            throw new ReservationAlreadyExistsException();
        }
        return reservationDao.findReservationById(id);
    }

    private void validateReservationOwnerName(String name, Reservation reservation) {
        if (!reservation.getName().equals(name)) {
            throw new ReservationOwnerMismatchException();
        }
    }

    private Reservation findReservation(Long id) {
        try {
            return reservationDao.findReservationById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ReservationNotFoundException();
        }
    }

    private ReservationTime findReservationTime(Long id) {
        try {
            return reservationTimeDao.findReservationTimeById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ReservationTimeNotFoundException();
        }
    }

    private void validatePastReservationCreate(LocalDate date, LocalTime startAt) {
        LocalDateTime present = LocalDateTime.now();
        LocalDateTime request = LocalDateTime.of(date, startAt);
        if (present.isAfter(request)) {
            throw new PastReservationNotAllowedException();
        }
    }

    private void validatePastReservationCancel(LocalDate date, LocalTime startAt) {
        LocalDateTime present = LocalDateTime.now();
        LocalDateTime request = LocalDateTime.of(date, startAt);
        if (present.isAfter(request)) {
            throw new PastReservationCancelNotAllowedException();
        }
    }
}
