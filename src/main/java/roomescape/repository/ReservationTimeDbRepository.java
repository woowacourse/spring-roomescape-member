package roomescape.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import roomescape.domain.ReservationTime;
import roomescape.repository.dao.ReservationTimeDao;

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

    public ReservationTime getById(Long id) {
        return findById(id)
                .orElseThrow(() -> new IllegalArgumentException("id에 해당하는 시간이 존재하지 않습니다."));
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
