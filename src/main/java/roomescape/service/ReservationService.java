package roomescape.service;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.dao.ThemeDao;
import roomescape.domain.exception.InvalidInputException;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

@Service
public class ReservationService {
    private final ReservationDao reservationDao;
    private final ReservationTimeDao reservationTimeDao;
    private final ThemeDao themeDao;
    private final Clock clock;

    public ReservationService(ReservationDao reservationDao, ReservationTimeDao reservationTimeDao, ThemeDao themeDao, Clock clock) {
        this.reservationDao = reservationDao;
        this.reservationTimeDao = reservationTimeDao;
        this.themeDao = themeDao;
        this.clock = clock;
    }

    @Transactional
    public Reservation save(String name, LocalDate date, long timeId, long themeId) {
        ReservationTime time = reservationTimeDao.findById(timeId)
                .orElseThrow(() -> new InvalidInputException("존재하지 않는 예약 시간입니다."));
        Theme theme = themeDao.findById(themeId)
                .orElseThrow(() -> new InvalidInputException("존재하지 않는 테마입니다."));
        if (reservationDao.existsByDateAndTimeIdAndThemeId(date, timeId, themeId)) {
            throw new ReservationConflictException("이미 예약된 시간입니다.");
        }
        Reservation reservation = Reservation.create(null, name, date, LocalDateTime.now(clock), time, theme);
        return reservationDao.save(reservation);
    }

    @Transactional
    public Reservation update(long id, LocalDate date, long timeId) {
        Reservation reservation = reservationDao.findById(id)
                .orElseThrow(() -> new ReservationNotFoundException("존재하지 않는 예약입니다."));
        ReservationTime newTime = reservationTimeDao.findById(timeId)
                .orElseThrow(() -> new InvalidInputException("존재하지 않는 예약 시간입니다."));
        Reservation updated = reservation.withUpdated(date, newTime, LocalDateTime.now(clock));
        if (reservationDao.existsByDateAndTimeIdAndThemeId(date, timeId, reservation.getTheme().getId())) {
            throw new ReservationConflictException("이미 예약된 시간입니다.");
        }
        return reservationDao.update(updated.getId(), date, timeId);
    }

    @Transactional
    public void delete(long id) {
        Optional<Reservation> found = reservationDao.findById(id);
        if (found.isEmpty()) {
            return;
        }
        found.get().validateCancellable(LocalDateTime.now(clock));
        reservationDao.delete(id);
    }

    @Transactional(readOnly = true)
    public List<Reservation> findAllByName(String username) {
        return reservationDao.findByName(username);
    }

    @Transactional(readOnly = true)
    public List<Reservation> findAll(int page, int size) {
        return reservationDao.findAll(page, size);
    }

    @Transactional(readOnly = true)
    public long countAll() {
        return reservationDao.count();
    }
}
