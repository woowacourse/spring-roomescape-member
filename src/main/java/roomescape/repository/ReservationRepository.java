package roomescape.repository;

import org.springframework.stereotype.Component;
import roomescape.dao.ReservationDao;
import roomescape.domain.Reservation;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
public class ReservationRepository {

    private final ReservationDao reservationDao;

    public ReservationRepository(ReservationDao reservationDao) {
        this.reservationDao = reservationDao;
    }

    public List<Reservation> findAll() {
        return reservationDao.findAll();
    }

    public Long save(Reservation reservation) {
        return reservationDao.save(reservation);
    }

    public void deleteById(Long id) {
        reservationDao.deleteById(id);
    }

    public boolean existsByTimeId(Long timeId) {
        return reservationDao.existsByTimeId(timeId);
    }

    public Optional<Reservation> findById(Long id) {
        return reservationDao.findById(id);
    }

    public boolean existsByThemeId(Long themeId) {
        return reservationDao.existsByThemeId(themeId);
    }

    public boolean existsBy(LocalDate date, Long timeId, Long themeId) {
        return reservationDao.existsBy(date, timeId, themeId);
    }

    public Set<Long> findReservedTimeIdsByDateAndThemeId(LocalDate date, Long themeId) {
        return reservationDao.findReservedTimeIdsByDateAndThemeId(date, themeId);
    }
}
