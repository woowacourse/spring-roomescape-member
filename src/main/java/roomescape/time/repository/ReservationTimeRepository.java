package roomescape.time.repository;

import java.util.List;
import org.springframework.stereotype.Repository;
import roomescape.time.domain.ReservationTime;
import roomescape.time.mapper.ReservationTimeMapper;
import roomescape.time.repository.dao.ReservationTimeDao;
import roomescape.time.repository.dto.CreateReservationTimeParams;

@Repository
public class ReservationTimeRepository {

    private final ReservationTimeDao reservationTimeDao;

    public ReservationTimeRepository(ReservationTimeDao reservationTimeDao) {
        this.reservationTimeDao = reservationTimeDao;
    }

    public ReservationTime save(CreateReservationTimeParams params) {
        Long id = reservationTimeDao.insert(params.startAt());
        return new ReservationTime(id, params.startAt());
    }

    public List<ReservationTime> findAll() {
        return reservationTimeDao.selectAll().stream()
                .map(ReservationTimeMapper::toReservationTime)
                .toList();
    }

    public void deleteById(Long id) {
        int deletedCount = reservationTimeDao.deleteById(id);

        if (deletedCount == 0) {
            throw new IllegalArgumentException("존재하지 않는 ID입니다");
        }
    }

    public ReservationTime findById(Long id) {
        return ReservationTimeMapper.toReservationTime(reservationTimeDao.findById(id));
    }
}
