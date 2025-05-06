package roomescape.repository;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;
import roomescape.dao.ReservationDao;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationRepository;
import roomescape.dto.response.ReservationResponse;

import java.time.LocalDate;
import java.util.List;

@Repository
public class JdbcReservationRepository implements ReservationRepository {

    private final ReservationDao reservationDao;

    public JdbcReservationRepository(final ReservationDao reservationDao) {
        this.reservationDao = reservationDao;
    }

    @Override
    public List<ReservationResponse> findAll() {
        return reservationDao.findAll()
                .stream()
                .map(ReservationResponse::from)
                .toList();
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
}
