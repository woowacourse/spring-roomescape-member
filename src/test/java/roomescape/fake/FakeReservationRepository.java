package roomescape.fake;

import roomescape.domain.model.Reservation;
import roomescape.domain.repository.ReservationRepository;

import java.time.LocalDate;
import java.util.List;

public class FakeReservationRepository implements ReservationRepository {

    private final FakeReservationDao reservationDao;

    public FakeReservationRepository() {
        this.reservationDao = new FakeReservationDao();
    }

    @Override
    public List<Reservation> findAll() {
        return reservationDao.findAll();
    }

    @Override
    public int deleteById(final Long id) {
        return reservationDao.deleteById(id);
    }

    @Override
    public Reservation save(final Reservation reservation) {
        return reservationDao.save(reservation);
    }

    @Override
    public boolean existByDateAndTimeIdAndThemeId(final LocalDate date, final Long timeId, final Long themeId) {
        return reservationDao.existByTimeIdAndThemeIdAndDate(timeId, themeId, date);
    }

    @Override
    public List<Long> findBookedTimes(final Long themeId, final LocalDate date) {
        return reservationDao.findBookedTimes(themeId, date);
    }

    @Override
    public boolean existByThemeId(final Long themeId) {
        return reservationDao.existByThemeId(themeId);
    }

    @Override
    public boolean existByTimeId(final Long timeId) {
        return reservationDao.existByTimeId(timeId);
    }

    @Override
    public List<Reservation> findByThemeIdAndMemberIdAndDate(final Long themeId, final Long memberId, final LocalDate dateFrom, final LocalDate dateTo) {
        return reservationDao.findByThemeIdAndMemberIdAndDate(themeId, memberId, dateFrom, dateTo);
    }

    public void clear() {
        reservationDao.clear();
    }
}
