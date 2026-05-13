package roomescape.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.domain.Reservation;
import roomescape.exception.PastReservationNotAllowedException;
import roomescape.exception.ReservationAlreadyExistsException;
import roomescape.exception.ReservationNotFoundException;

@Service
@Transactional(readOnly = true)
public class ReservationService {

    private final ReservationDao reservationDao;
    private final ReservationTimeDao reservationTimeDao;


    public ReservationService(ReservationDao reservationDao, ReservationTimeDao reservationTimeDao) {
        this.reservationDao = reservationDao;
        this.reservationTimeDao = reservationTimeDao;
    }

    public List<Reservation> getReservations() {
        return reservationDao.findAllReservations();
    }

    @Transactional
    public Reservation createReservation(String name, LocalDate date, Long timeId, Long themeId) {
        LocalTime startAt = reservationTimeDao.findReservationTimeById(timeId).getStartAt();
        validatePastTime(date, startAt);
        try {
            Long id = reservationDao.insertWithKeyHolder(name, date, timeId, themeId);
            return reservationDao.findReservationById(id);
        } catch (DuplicateKeyException e) {
            throw new ReservationAlreadyExistsException();
        }
    }

    @Transactional
    public void deleteReservation(Long id) {
        int deleteCount = reservationDao.delete(id);
        validateDelete(deleteCount);
    }

    private void validateDelete(int deleteCount) {
        if (deleteCount == 0) {
            throw new ReservationNotFoundException();
        }
    }

    private void validatePastTime(LocalDate date, LocalTime startAt) {
        LocalDateTime present = LocalDateTime.now();
        LocalDateTime request = LocalDateTime.of(date, startAt);
        if (present.isAfter(request)) {
            throw new PastReservationNotAllowedException();
        }
    }
}
