package roomescape.service.fake;

import java.time.LocalDate;
import java.util.List;
import roomescape.dao.ThemeDao;
import roomescape.domain.Theme;
import roomescape.domain.vo.Name;
import roomescape.dto.request.PopularThemeRequestDto;
import roomescape.dto.response.AvailableTimeResponseDto;

public class FakeThemeDao extends FakeCommonDao<Theme> implements ThemeDao {
    
    @Override
    public boolean existsByName(Name name) {
        return store.values()
                .stream()
                .anyMatch(theme -> theme.getName().equals(name));
    }

    @Override
    public List<AvailableTimeResponseDto> findAvailableTimesById(Long themeId, LocalDate localDate) {
        return List.of();
    }

    @Override
    public List<Theme> findPopulars(PopularThemeRequestDto popularThemeRequestDto) {
        return List.of();
    }

    @Override
    Theme create(Theme theme, Long id) {
        return new Theme(id, theme.getName(), theme.getThumbnailUrl(), theme.getDescription());
    }
}
