package roomescape.repository;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import roomescape.global.exception.ThemeNotFoundException;
import roomescape.domain.Theme;
import roomescape.repository.dao.ThemeDao;

@Repository
@RequiredArgsConstructor
public class ThemeRepository {

    private final ThemeDao themeDao;

    public List<Theme> findAll() {
        return themeDao.selectAll();
    }

    public Theme save(Theme theme) {
        return themeDao.insert(theme);
    }

    public void deleteById(Long id) {
        int deletedCount = themeDao.deleteById(id);

        if (deletedCount == 0) {
            throw new ThemeNotFoundException("존재하지 않는 테마 번호입니다.");
        }
    }

    public Theme findById(Long id) {
        return themeDao.selectById(id)
                .orElseThrow(ThemeNotFoundException::new);
    }

    public List<Theme> findThemesOrderedByReservationCount(java.time.LocalDate startDate,
                                                           java.time.LocalDate endDate,
                                                           int limit) {
        return themeDao.findThemesOrderByReservationCountDesc(startDate, endDate, limit);
    }
}
