package roomescape.theme.repository;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import roomescape.global.exception.validation.ThemeNotFoundException;
import roomescape.theme.domain.Theme;
import roomescape.theme.mapper.ThemeMapper;
import roomescape.theme.repository.dao.ThemeDao;
import roomescape.theme.repository.dto.CreateThemeParams;
import roomescape.theme.repository.entity.ThemeEntity;
import roomescape.theme.repository.dto.GetThemeRankingsInRecentDaysParams;

@Repository
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ThemeRepository {

    private final ThemeDao themeDao;

    @Transactional
    public Theme save(CreateThemeParams params) {
        Long id = themeDao.insert(params.name(), params.description(), params.imageURl());
        ThemeEntity themeEntity = new ThemeEntity(id, params.name(), params.description(), params.imageURl(), false);
        return ThemeMapper.toTheme(themeEntity);
    }

    @Transactional
    public void deleteById(Long id) {
        int deletedCount = themeDao.deleteById(id);

        if (deletedCount == 0) {
            throw new ThemeNotFoundException();
        }
    }

    public boolean existsById(Long id) {
        return themeDao.existsById(id);
    }

    public List<Theme> findAll() {
        return themeDao.findAll().stream()
                .map(ThemeMapper::toTheme)
                .toList();
    }

    public Theme findById(Long id) {
        ThemeEntity themeEntity = themeDao.findById(id)
                .orElseThrow(ThemeNotFoundException::new);
        return ThemeMapper.toTheme(themeEntity);
    }

    public List<Theme> findThemesOrderedByReservationCount(GetThemeRankingsInRecentDaysParams params) {
        return themeDao.findThemesOrderByReservationCountDesc(params.startDate(), params.endDate(), params.limit())
                .stream()
                .map(ThemeMapper::toTheme)
                .toList();
    }
}
