package roomescape.repository;

import org.springframework.dao.EmptyResultDataAccessException;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationRepository;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.response.ReservationResponse;
import roomescape.exception.ResourceNotExistException;
import roomescape.fake.FakeReservationDao;
import roomescape.fake.FakeReservationTimeDao;
import roomescape.fake.FakeThemeDao;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class FakeReservationRepository implements ReservationRepository {

    private final FakeReservationDao reservationDao;
    private final FakeReservationTimeDao reservationTimeDao;
    private final FakeThemeDao themeDao;

    public FakeReservationRepository(final FakeReservationDao reservationDao, final FakeReservationTimeDao reservationTimeDao, final FakeThemeDao themeDao) {
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
    public int deleteReservationById(final Long id) {
        return reservationDao.deleteById(id);
    }

    @Override
    public ReservationTime findReservationTimeById(final Long timeId) {
        try {
            return reservationTimeDao.findById(timeId);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotExistException();
        }
    }

    @Override
    public Theme findThemeById(final Long themeId) {
        return themeDao.findById(themeId);
    }

    @Override
    public Reservation saveReservation(final Reservation reservation) {
        return reservationDao.save(reservation);
    }

    @Override
    public boolean existByTimeIdAndThemeIdAndDate(final Long timeId, final Long themeId, final LocalDate date) {
        return reservationDao.existByTimeIdAndThemeIdAndDate(timeId, themeId, date);
    }

    @Override
    public ReservationTime saveReservationTime(final ReservationTime reservationTime) {
        return reservationTimeDao.save(reservationTime);
    }

    @Override
    public List<ReservationTime> findAllReservationTimes() {
        return reservationTimeDao.findAll();
    }

    @Override
    public int deleteReservationTimeById(final Long id) {
        return reservationTimeDao.deleteById(id);
    }

    @Override
    public List<Theme> findAllThemes() {
        return themeDao.findAll();
    }

    @Override
    public int deleteThemeById(final Long id) {
        return themeDao.deleteById(id);
    }

    @Override
    public List<Theme> findPopularThemes(final int count) {
        return themeDao.findPopular(reservationDao.findAll(), count);
    }

    @Override
    public boolean existByName(final String name) {
        return themeDao.existByName(name);
    }

    @Override
    public Theme saveTheme(final Theme theme) {
        return themeDao.save(theme);
    }

    @Override
    public List<Long> findBookedTimes(final Long themeId, final LocalDate date) {
        return reservationDao.findBookedTimes(themeId, date);
    }

    @Override
    public boolean existReservationByThemeId(final Long themeId) {
        return reservationDao.existByThemeId(themeId);
    }

    @Override
    public boolean existReservationByTimeId(final Long timeId) {
        return reservationDao.existByTimeId(timeId);
    }

    @Override
    public boolean existReservationTimeByTimeValue(final LocalTime localTime) {
        return reservationTimeDao.existByTimeValue(localTime);
    }
}
