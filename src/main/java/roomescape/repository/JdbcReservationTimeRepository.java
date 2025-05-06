package roomescape.repository;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;
import roomescape.dao.ReservationTimeDao;
import roomescape.domain.ReservationTime;
import roomescape.domain.ReservationTimeRepository;
import roomescape.exception.ResourceNotExistException;

import java.time.LocalTime;
import java.util.List;

@Repository
public class JdbcReservationTimeRepository implements ReservationTimeRepository {

    private final ReservationTimeDao reservationTimeDao;

    public JdbcReservationTimeRepository(final ReservationTimeDao reservationTimeDao) {
        this.reservationTimeDao = reservationTimeDao;
    }

    @Override
    public ReservationTime findById(final Long timeId) {
        try {
            return reservationTimeDao.findById(timeId);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotExistException();
        }
    }

    @Override
    public ReservationTime save(final ReservationTime reservationTime) {
        try {
            return reservationTimeDao.save(reservationTime);
        } catch (DuplicateKeyException e) {
            throw new IllegalArgumentException("[ERROR] 해당 시간이 이미 존재합니다.");
        } catch (DataAccessException e) {
            throw new IllegalArgumentException("[ERROR] 예약 시간 생성에 실패하였습니다");
        }
    }

    @Override
    public List<ReservationTime> findAll() {
        return reservationTimeDao.findAll();
    }

    @Override
    public int deleteById(final Long id) {
        try {
            return reservationTimeDao.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("[ERROR] 해당 시간에 대한 예약이 존재하기 때문에 삭제할 수 없습니다.");
        }
    }

    @Override
    public boolean existByTimeValue(final LocalTime localTime) {
        return reservationTimeDao.existByTimeValue(localTime);
    }
}
