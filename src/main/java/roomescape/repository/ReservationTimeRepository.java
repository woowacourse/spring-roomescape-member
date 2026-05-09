package roomescape.repository;

import org.springframework.stereotype.Component;
import roomescape.dao.ReservationTimeDao;
import roomescape.domain.ReservationTime;

import java.util.List;
import java.util.Optional;

@Component
public class ReservationTimeRepository {

    private final ReservationTimeDao timeDao;

    public ReservationTimeRepository(ReservationTimeDao timeDao) {
        this.timeDao = timeDao;
    }

    public List<ReservationTime> findAll() {
        return timeDao.findAll();
    }

    public ReservationTime save(ReservationTime time) {
        return timeDao.save(time);
    }

    public void deleteById(Long id) {
        timeDao.deleteById(id);
    }

    public boolean existsById(Long id) {
        return timeDao.existsById(id);
    }

    public Optional<ReservationTime> findById(Long id) {
        return timeDao.findById(id);
    }
}
