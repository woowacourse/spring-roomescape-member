package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.dao.ReservationDao;
import roomescape.domain.Reservation;
import roomescape.exception.ReservationAlreadyExistsException;
import roomescape.exception.ReservationNotFoundException;

@Service
@Transactional(readOnly = true)
public class ReservationService {

    private final ReservationDao reservationDao;

    public ReservationService(ReservationDao reservationDao) {
        this.reservationDao = reservationDao;
    }

    public List<Reservation> getReservations() {
        return reservationDao.findAllReservations();
    }

    @Transactional
    public Reservation createReservation(String name, LocalDate date, Long timeId, Long themeId) {
        try {
            Long id = reservationDao.insertReservation(name, date, timeId, themeId);
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
}
