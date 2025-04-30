package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dao.ThemeDao;
import roomescape.dto.ThemeRequest;
import roomescape.dto.ThemeResponse;
import roomescape.model.Theme;

@Service
public class ThemeService {
    private final ThemeDao themeDao;

    public ThemeService(ThemeDao themeDao) {
        this.themeDao = themeDao;
    }

    public ThemeResponse addTheme(ThemeRequest themeRequest) {
        Theme saved = themeDao.save(themeRequest.toEntity());
        return ThemeResponse.from(saved);
    }

    public List<ThemeResponse> getThemes() {
        return themeDao.findAll().stream()
                .map(ThemeResponse::from)
                .toList();
    }

    public void deleteTheme(Long id) {
        boolean isDeleted = themeDao.deleteById(id);
        if (!isDeleted) {
            throw new IllegalArgumentException("해당하는 ID가 없습니다.");
        }
    }
}
