package roomescape.repository;

import org.springframework.stereotype.Component;
import roomescape.dao.ReservationTimeDao;
import roomescape.domain.ReservationTime;

import java.util.List;

@Component
public class ReservationTimeRepository {

    private final ReservationTimeDao timeDao;

    public ReservationTimeRepository(ReservationTimeDao timeDao) {
        this.timeDao = timeDao;
    }

    public List<ReservationTime> findAll() {
        return timeDao.findAll();
    }

    public Long save(ReservationTime time) {
        return timeDao.save(time);
    }

    public void deleteById(Long id) {
        timeDao.deleteById(id);
    }

    public boolean existsById(Long id) {
        return timeDao.existsById(id);
    }
}
