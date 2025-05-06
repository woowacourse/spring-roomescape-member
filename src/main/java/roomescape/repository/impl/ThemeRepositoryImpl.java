package roomescape.repository.impl;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Component;
import roomescape.dao.ReservationDao;
import roomescape.dao.ThemeDao;
import roomescape.domain.Theme;
import roomescape.exception.InvalidReservationException;
import roomescape.repository.ThemeRepository;

@Component
public class ThemeRepositoryImpl implements ThemeRepository {

    private final ThemeDao themeDao;
    private final ReservationDao reservationDao;

    public ThemeRepositoryImpl(ThemeDao themeDao, ReservationDao reservationDao) {
        this.themeDao = themeDao;
        this.reservationDao = reservationDao;
    }

    @Override
    public List<Theme> findAll() {
        return themeDao.findAll();
    }

    @Override
    public void save(Theme theme) {
        long savedId = themeDao.save(theme);
        theme.setId(savedId);
    }

    @Override
    public void delete(Long id) {
        findById(id);
        if (reservationDao.countExistReservationByTheme(id) != 0) {
            throw new InvalidReservationException("이미 예약된 테마를 삭제할 수 없습니다.");
        }
        themeDao.delete(id);
    }

    @Override
    public Theme findById(Long id) {
        return themeDao.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("해당 ID의 테마를 찾을 수 없습니다"));
    }

    @Override
    public List<Theme> calculateRankForReservationAmount(LocalDate startDate,
        LocalDate currentDate) {
        return themeDao.calculateRankForReservationAmount(startDate, currentDate);
    }
}
