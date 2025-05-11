package roomescape.infrastructure.repository;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;
import roomescape.domain.exception.ReservationTimeDuplicatedException;
import roomescape.domain.exception.ResourceNotExistException;
import roomescape.domain.model.ReservationTime;
import roomescape.domain.repository.ReservationTimeRepository;
import roomescape.infrastructure.dao.ReservationTimeDao;

import java.time.LocalTime;
import java.util.List;

@Repository
public class ReservationTimeRepositoryImpl implements ReservationTimeRepository {

    private final ReservationTimeDao reservationTimeDao;

    public ReservationTimeRepositoryImpl(final ReservationTimeDao reservationTimeDao) {
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
            throw new ReservationTimeDuplicatedException();
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
            throw new IllegalArgumentException();
        }
    }

    @Override
    public boolean existByTimeValue(final LocalTime localTime) {
        return reservationTimeDao.existByTimeValue(localTime);
    }
}
