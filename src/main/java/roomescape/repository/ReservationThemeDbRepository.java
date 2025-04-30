package roomescape.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import roomescape.domain.ReservationTheme;
import roomescape.repository.dao.ReservationThemeDao;

@Repository
@RequiredArgsConstructor
public class ReservationThemeDbRepository implements ReservationThemeRepository {

    private final ReservationThemeDao reservationThemeDao;

    @Override
    public List<ReservationTheme> getAll() {
        return reservationThemeDao.selectAll();
    }

    @Override
    public ReservationTheme save(ReservationTheme reservationTime) {
        return reservationThemeDao.insertAndGet(reservationTime);
    }

    @Override
    public Optional<ReservationTheme> findById(Long id) {
        return reservationThemeDao.selectById(id);
    }

    @Override
    public ReservationTheme getById(Long id) {
        return findById(id)
                .orElseThrow(() -> new IllegalArgumentException("id에 해당하는 테마가 존재하지 않습니다."));
    }

    @Override
    public void remove(ReservationTheme reservationTime) {
        reservationThemeDao.deleteById(reservationTime.id());
    }

    @Override
    public List<ReservationTheme> orderByThemeBookedCountWithLimit(int limit) {
        return reservationThemeDao.orderByThemeBookedCountWithLimit(limit);
    }
}
