package roomescape.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.dao.ThemeDao;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.exception.PastReservationException;
import roomescape.exception.ReservationConflictException;

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

    @Transactional
    public Reservation save(String name, LocalDate date, long timeId, long themeId) {
        final ReservationTime time = reservationTimeDao.findById(timeId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 예약 시간입니다."));
        final Theme theme = themeDao.findById(themeId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테마입니다."));
        if (reservationDao.existsByDateAndTimeIdAndThemeId(date, timeId, themeId)) {
            throw new ReservationConflictException("이미 예약된 시간입니다.");
        }
        final Reservation reservation = Reservation.create(name, date, LocalDateTime.now(), time, theme);
        return reservationDao.save(reservation);
    }

    @Transactional
    public Reservation update(long id, LocalDate date, long timeId) {
        Reservation reservation = reservationDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 예약입니다."));
        final ReservationTime newTime = reservationTimeDao.findById(timeId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 예약 시간입니다."));
        if (LocalDateTime.of(date, newTime.getStartAt()).isBefore(LocalDateTime.now())) {
            throw new PastReservationException("과거 날짜로는 예약할 수 없습니다.");
        }
        if (reservationDao.existsByDateAndTimeIdAndThemeId(date, timeId, reservation.getTheme().getId())) {
            throw new ReservationConflictException("이미 예약된 시간입니다.");
        }
        return reservationDao.update(id, date, timeId);
    }

    @Transactional
    public void delete(long id) {
        Reservation reservation = reservationDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 예약입니다."));
        if (LocalDateTime.of(reservation.getDate(), reservation.getTime().getStartAt()).isBefore(LocalDateTime.now())) {
            throw new PastReservationException("이미 지난 예약은 취소할 수 없습니다.");
        }
        reservationDao.delete(id);
    }

    @Transactional(readOnly = true)
    public List<Reservation> findAllByName(String username) {
        return reservationDao.findByName(username);
    }

    @Transactional(readOnly = true)
    public List<Reservation> findAll() {
        return reservationDao.findAll();
    }
}
