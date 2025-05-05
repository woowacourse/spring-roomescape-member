package roomescape.infrastructure.db;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import roomescape.domain.entity.ReservationTime;
import roomescape.domain.repository.ReservationTimeRepository;
import roomescape.global.exception.ResourceNotFoundException;
import roomescape.infrastructure.db.dao.ReservationTimeDao;

@Repository
@RequiredArgsConstructor
public class ReservationTimeDbRepository implements ReservationTimeRepository {

    private final ReservationTimeDao reservationTimeDao;

    @Override
    public List<ReservationTime> getAll() {
        return reservationTimeDao.selectAll();
    }

    @Override
    public ReservationTime save(ReservationTime reservationTime) {
        return reservationTimeDao.insertAndGet(reservationTime);
    }

    @Override
    public Optional<ReservationTime> findById(Long id) {
        return reservationTimeDao.selectById(id);
    }

    @Override
    public ReservationTime getById(Long id) {
        return findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("id에 해당하는 시간이 존재하지 않습니다."));
    }

    @Override
    public void remove(ReservationTime reservationTime) {
        reservationTimeDao.deleteById(reservationTime.id());
    }

    @Override
    public List<ReservationTime> getAllByThemeIdAndDate(Long themeId, LocalDate date) {
        return reservationTimeDao.selectAllByThemeIdAndDate(themeId, date);
    }
}
