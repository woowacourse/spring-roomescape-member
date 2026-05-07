package roomescape.theme.repository;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import roomescape.theme.domain.Theme;
import roomescape.theme.mapper.ThemeMapper;
import roomescape.theme.repository.dao.ThemeDao;
import roomescape.theme.repository.dto.CreateThemeParams;
import roomescape.theme.repository.entity.ThemeEntity;
import roomescape.theme.service.GetThemeRankingsInRecentDaysParams;

@Repository
@RequiredArgsConstructor
public class ThemeRepository {

    private final ThemeDao themeDao;

    public List<Theme> findAll() {
        return themeDao.selectAll().stream()
                .map(ThemeMapper::toTheme)
                .toList();
    }

    public Theme save(CreateThemeParams params) {
        Long id = themeDao.insert(params.name(), params.description(), params.imageURl());
        ThemeEntity themeEntity = new ThemeEntity(id, params.name(), params.description(), params.imageURl(), false);
        return ThemeMapper.toTheme(themeEntity);
    }

    public void deleteById(Long id) {
        int deletedCount = themeDao.deleteById(id);

        if (deletedCount == 0) {
            throw new IllegalArgumentException("존재하지 않는 테마 번호입니다.");
        }
    }

    public Theme findById(Long id) {
        ThemeEntity themeEntity = themeDao.selectById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테마입니다."));
        return ThemeMapper.toTheme(themeEntity);
    }

    public List<Theme> findThemesOrderedByReservationCount(GetThemeRankingsInRecentDaysParams params) {
        return themeDao.findThemesOrderByReservationCountDesc(params.startDate(), params.endDate(), params.limit())
                .stream()
                .map(ThemeMapper::toTheme)
                .toList();
    }
}
