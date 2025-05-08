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
public class ReservationTimeRepositoryImpl implements ReservationTimeRepository {

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
        return findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 예약 시간입니다."));
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
