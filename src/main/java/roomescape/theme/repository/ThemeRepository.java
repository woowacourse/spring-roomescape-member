package roomescape.theme.repository;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
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

    public List<Theme> findAll() {
        return themeDao.findAll().stream()
                .map(ThemeMapper::toTheme)
                .toList();
    }

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
            throw new IllegalArgumentException("존재하지 않는 테마 번호입니다.");
        }
    }

    public Theme findById(Long id) {
        ThemeEntity themeEntity = themeDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테마입니다."));
        return ThemeMapper.toTheme(themeEntity);
    }

    public boolean existsById(Long id) {
        return themeDao.existsById(id);
    }

    public List<Theme> findThemesOrderedByReservationCount(GetThemeRankingsInRecentDaysParams params) {
        return themeDao.findThemesOrderByReservationCountDesc(params.startDate(), params.endDate(), params.limit())
                .stream()
                .map(ThemeMapper::toTheme)
                .toList();
    }
}
