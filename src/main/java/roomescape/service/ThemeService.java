package roomescape.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.dao.ThemeDao;
import roomescape.domain.theme.ThemeAggregationLimit;
import roomescape.domain.theme.ThemeAggregationPeriod;
import roomescape.domain.theme.Theme;
import roomescape.dto.theme.ThemeResponse;

import java.time.LocalDate;
import java.util.List;

@Transactional
@Service
public class ThemeService {

    private final ThemeDao themeDao;

    public ThemeService(final ThemeDao themeDao) {
        this.themeDao = themeDao;
    }

    public ThemeResponse create(final Theme theme) {
        return ThemeResponse.from(themeDao.save(theme));
    }

    @Transactional(readOnly = true)
    public List<ThemeResponse> findAll() {
        final List<Theme> themes = themeDao.findAll();
        return themes.stream()
                .map(ThemeResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public ThemeResponse findById(final Long id) {
        final Theme theme = themeDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(id + "에 해당하는 테마가 없습니다."));
        return ThemeResponse.from(theme);
    }

    public void deleteById(final Long id) {
        final Theme theme = themeDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(id + "에 해당하는 테마가 없습니다."));
        themeDao.deleteById(theme.getId());
    }

    @Transactional(readOnly = true)
    public List<ThemeResponse> findPopularThemes() {
        final LocalDate period = ThemeAggregationPeriod.calculateAggregationPeriod(LocalDate.now());
        final int limit = ThemeAggregationLimit.getAggregationLimit();

        final List<Theme> allOrderByReservationCountInLastWeek
                = themeDao.findTopThemesByReservationCountDuringPeriod(period, limit);

        return allOrderByReservationCountInLastWeek.stream()
                .map(ThemeResponse::from)
                .toList();
    }
}
