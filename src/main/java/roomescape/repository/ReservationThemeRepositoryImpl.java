package roomescape.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import roomescape.domain.ReservationTheme;
import roomescape.repository.dao.ReservationThemeDao;

@Repository
@RequiredArgsConstructor
public class ReservationThemeRepositoryImpl implements ReservationThemeRepository {

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
        return findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테마입니다."));
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
