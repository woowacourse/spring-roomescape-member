package roomescape.service;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.dao.ThemeDao;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.exception.DuplicateResourceException;
import roomescape.exception.InvalidInputException;
import roomescape.exception.NotFoundException;
import roomescape.exception.PastReservationException;

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

    public List<Reservation> find(String name, int page, int size) {
        if (name == null) {
            return reservationDao.findAll(page, size);
        }
        if (name.isBlank()) {
            throw new InvalidInputException("예약자 이름은 필수입니다.");
        }
        return reservationDao.findByName(name, page, size);
    }

    public void cancelById(long id) {
        Reservation reservation = reservationDao.findById(id);
        LocalDateTime now = LocalDateTime.now(clock);

        if (reservation.isPast(now)) {
            throw new PastReservationException("지난 예약은 취소할 수 없습니다.");
        }
        reservationDao.deleteById(id);
    }

    public Reservation save(String name, LocalDate date, Long timeId, Long themeId) {
        if (!reservationTimeDao.existsById(timeId)) {
            throw new NotFoundException("존재하지 않는 예약 시간입니다.");
        }

        if (!themeDao.existsById(themeId)) {
            throw new NotFoundException("존재하지 않는 테마입니다.");
        }

        if (reservationDao.existByDateAndTimeAndThemeId(date, timeId, themeId)) {
            throw new DuplicateResourceException(
                    "DUPLICATE_RESERVATION",
                    "이미 존재하는 예약입니다."
            );
        }
        ReservationTime time = reservationTimeDao.findById(timeId);

        Theme theme = themeDao.findById(themeId);
        Reservation reservation = Reservation.create(
                name,
                date,
                time,
                theme,
                LocalDateTime.now(clock)
        );
        return reservationDao.save(reservation);
    }

    public Reservation updateDateAndTime(long id, LocalDate date, Long timeId) {
        if (!reservationDao.existById(id)) {
            throw new NotFoundException("존재하지 않는 예약입니다.");
        }

        Reservation reservation = reservationDao.findById(id);
        LocalDateTime now = LocalDateTime.now(clock);

        if (reservation.isPast(now)) {
            throw new PastReservationException("지난 예약은 변경할 수 없습니다.");
        }

        if (!reservationTimeDao.existsById(timeId)) {
            throw new NotFoundException("존재하지 않는 예약 시간입니다.");
        }

        ReservationTime newTime = reservationTimeDao.findById(timeId);

        if (newTime.isPast(date, now)) {
            throw new PastReservationException("지난 날짜 또는 시간으로 변경할 수 없습니다.");
        }

        if (reservationDao.existByDateAndTimeAndThemeId(date, timeId, reservation.getThemeId())) {
            throw new DuplicateResourceException(
                    "DUPLICATE_RESERVATION",
                    "이미 존재하는 예약입니다."
            );
        }

        reservationDao.updateDateAndTime(id, date, timeId);
        return reservationDao.findById(id);
    }

    public void deleteById(Long id) {
        reservationDao.deleteById(id);
    }
}
