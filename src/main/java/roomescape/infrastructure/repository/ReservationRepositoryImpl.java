package roomescape.infrastructure.repository;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;
import roomescape.application.dto.ReservationInfoResponse;
import roomescape.domain.exception.ReservationDuplicatedException;
import roomescape.domain.model.Reservation;
import roomescape.domain.repository.ReservationRepository;
import roomescape.infrastructure.dao.ReservationDao;

import java.time.LocalDate;
import java.util.List;

@Repository
public class ReservationRepositoryImpl implements ReservationRepository {

    private final ReservationDao reservationDao;

    public ReservationRepositoryImpl(final ReservationDao reservationDao) {
        this.reservationDao = reservationDao;
    }

    @Override
    public List<ReservationInfoResponse> findAll() {
        return reservationDao.findAll();
    }

    @Override
    public int deleteById(final Long id) {
        return reservationDao.deleteById(id);
    }

    @Override
    public Reservation save(final Reservation reservation) {
        try {
            return reservationDao.save(reservation);
        } catch (DuplicateKeyException e) {
            throw new ReservationDuplicatedException();
        } catch (DataAccessException e) {
            throw new IllegalArgumentException("[ERROR] 예약 생성에 실패하였습니다");
        }
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
    public List<ReservationInfoResponse> findByThemeIdAndMemberIdAndDate(final Long themeId, final Long memberId, final LocalDate dateFrom, final LocalDate dateTo) {
        return reservationDao.findByThemeIdAndMemberIdAndDate(themeId, memberId, dateFrom, dateTo);
    }
}
