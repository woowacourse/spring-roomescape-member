package roomescape.repository;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.dao.ThemeDao;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationRepository;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.response.ReservationResponse;
import roomescape.exception.ForeignKeyConstraintViolationException;
import roomescape.exception.ResourceNotExistException;

import java.time.LocalDate;
import java.util.List;

@Repository
public class JdbcReservationRepository implements ReservationRepository {

    private final ReservationDao reservationDao;
    private final ReservationTimeDao reservationTimeDao;
    private final ThemeDao themeDao;

    public JdbcReservationRepository(final ReservationDao reservationDao, final ReservationTimeDao reservationTimeDao, final ThemeDao themeDao) {
        this.reservationDao = reservationDao;
        this.reservationTimeDao = reservationTimeDao;
        this.themeDao = themeDao;
    }

    @Override
    public List<ReservationResponse> findAllReservations() {
        return reservationDao.findAll()
                .stream()
                .map(ReservationResponse::from)
                .toList();
    }

    @Override
    public void deleteReservationById(final Long id) {
        int deleteCount = reservationDao.deleteById(id);
        if (deleteCount == 0) {
            throw new ResourceNotExistException();
        }
    }

    @Override
    public ReservationTime findReservationTimeById(final Long timeId) {
        try {
            return reservationTimeDao.findById(timeId);
        } catch (EmptyResultDataAccessException e) {
            throw new IllegalArgumentException("[ERROR] 예약 시간이 존재하지 않습니다.");
        }
    }

    @Override
    public Theme findThemeById(final Long themeId) {
        try {
            return themeDao.findById(themeId);
        } catch (EmptyResultDataAccessException e) {
            throw new IllegalArgumentException("[ERROR] 예약 테마가 존재하지 않습니다.");
        }
    }

    @Override
    public Reservation saveReservation(final Reservation reservation) {
        try {
            return reservationDao.save(reservation);
        } catch (DuplicateKeyException e) {
            throw new IllegalArgumentException("[ERROR] 해당 날짜와 시간에 대한 예약이 이미 존재합니다.");
        } catch (DataAccessException e) {
            throw new IllegalArgumentException("[ERROR] 예약 생성에 실패하였습니다");
        }
    }

    @Override
    public boolean existByTimeIdAndThemeIdAndDate(final Long timeId, final Long themeId, final LocalDate date) {
        return reservationDao.existByTimeIdAndThemeIdAndDate(timeId, themeId, date);
    }

    @Override
    public ReservationTime saveReservationTime(final ReservationTime reservationTime) {
        try {
            return reservationTimeDao.save(reservationTime);
        } catch (DataAccessException e) {
            throw new IllegalArgumentException("[ERROR] 예약 시간 생성에 실패하였습니다");
        }
    }

    @Override
    public List<ReservationTime> findAllReservationTimes() {
        return reservationTimeDao.findAll();
    }

    @Override
    public void deleteReservationTimeById(final Long id) {
        validateTimeIsInUse(id);
        int deleteCount = reservationTimeDao.deleteById(id);
        if (deleteCount == 0) {
            throw new ResourceNotExistException();
        }
    }

    @Override
    public List<Theme> findAllThemes() {
        return themeDao.findAll();
    }

    @Override
    public void deleteThemeById(final Long id) {
        validateThemeIsInUse(id);
        int count = themeDao.deleteById(id);
        if (count == 0) {
            throw new ResourceNotExistException();
        }
    }

    @Override
    public List<Theme> findPopularThemes(final int count) {
        return themeDao.findPopular(count);
    }

    @Override
    public boolean existByName(final String name) {
        return themeDao.existByName(name);
    }

    @Override
    public Theme saveTheme(final Theme theme) {
        try {
            return themeDao.save(theme);
        } catch (DuplicateKeyException e) {
            throw new IllegalArgumentException("[ERROR] 해당 테마 이름이 이미 존재합니다.");
        } catch (DataAccessException e) {
            throw new IllegalArgumentException("[ERROR] 테마 생성에 실패했습니다.");
        }
    }

    @Override
    public List<Long> findBookedTimes(final Long themeId, final LocalDate date) {
        return reservationDao.findBookedTimes(themeId, date);
    }

    private void validateThemeIsInUse(Long id) {
        if (reservationDao.existByThemeId(id)) {
            throw new ForeignKeyConstraintViolationException();
        }
    }

    private void validateTimeIsInUse(final Long id) {
        if (reservationDao.existByTimeId(id)) {
            throw new ForeignKeyConstraintViolationException();
        }
    }
}
