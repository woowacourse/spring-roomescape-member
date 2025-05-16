package roomescape.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import roomescape.domain.ReservationTheme;
import roomescape.repository.dao.ReservationThemeDao;

@Repository
@RequiredArgsConstructor
public class ReservationThemeRepository {

    private final ReservationThemeDao reservationThemeDao;

    public List<ReservationTheme> getAll() {
        return reservationThemeDao.selectAll();
    }

    public ReservationTheme save(ReservationTheme reservationTheme) {
        return reservationThemeDao.insertAndGet(reservationTheme);
    }

    public Optional<ReservationTheme> findById(Long id) {
        return reservationThemeDao.selectById(id);
    }

    public ReservationTheme getById(Long id) {
        return findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테마입니다."));
    }

    public void remove(ReservationTheme reservationTheme) {
        reservationThemeDao.deleteById(reservationTheme.id());
    }

    public List<ReservationTheme> orderByThemeBookedCountWithLimit(int limit) {
        return reservationThemeDao.orderByThemeBookedCountWithLimit(limit);
    }
}
