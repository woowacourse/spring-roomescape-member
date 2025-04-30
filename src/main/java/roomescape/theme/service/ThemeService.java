package roomescape.theme.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.common.Dao;
import roomescape.theme.domain.Theme;
import roomescape.theme.dto.ThemeRequest;
import roomescape.theme.dto.ThemeResponse;

@Service
public class ThemeService {
    private final Dao<Theme> themeDao;

    public ThemeService(Dao<Theme> themeDao) {
        this.themeDao = themeDao;
    }

    public List<ThemeResponse> findAll() {
        return themeDao.findAll().stream()
                .map(theme -> new ThemeResponse(
                        theme.getId(),
                        theme.getName(),
                        theme.getDescription(),
                        theme.getThumbnail()))
                .toList();
    }

    public ThemeResponse add(ThemeRequest themeRequest) {
        // TODO : 검증 로직 추가
        Theme theme = new Theme(null, themeRequest.name(), themeRequest.description(), themeRequest.thumbnail());
        Theme savedTheme = themeDao.add(theme);
        return new ThemeResponse(savedTheme.getId(),
                savedTheme.getName(),
                savedTheme.getDescription(),
                savedTheme.getThumbnail());
    }

    public void deleteById(Long id) {
        // TODO : 검증 로직 추가
        themeDao.deleteById(id);
    }
}
